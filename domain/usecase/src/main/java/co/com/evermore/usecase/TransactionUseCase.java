package co.com.evermore.usecase;

import co.com.evermore.model.common.ex.BusinessException;
import co.com.evermore.model.provider.ProviderTransaction;
import co.com.evermore.model.provider.ProviderTransactionResponse;
import co.com.evermore.model.provider.gateways.ProviderTransactionGateway;
import co.com.evermore.model.transaction.Transaction;
import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.model.transactionhistory.gateways.TransactionHistoryRepository;
import co.com.evermore.model.user.User;
import co.com.evermore.model.user.UserBankAccount;
import co.com.evermore.model.user.gateways.UserRepository;
import co.com.evermore.model.wallet.WalletBalance;
import co.com.evermore.model.wallet.gateways.WalletGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import static java.util.Objects.isNull;

@Log
@RequiredArgsConstructor
public class TransactionUseCase {

    private final WalletGateway walletGateway;
    private final ProviderTransactionGateway providerGateway;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final BigDecimal onTopFee;

    public Mono<TransactionHistory> createNewTransaction(Transaction transaction) {

        log.info("Starting a new transaction with the info: " + transaction);

        return transactionFieldsValidation(transaction)
                .flatMap(validatedTransaction -> userRepository.findUserById(validatedTransaction.getUserId()))
                .flatMap(user -> buildProviderTransaction(transaction, user))
                .flatMap(providerTransaction -> walletGateway.getUserBalance(providerTransaction.getUserId())
                        .flatMap(walletBalance -> balanceValidation(providerTransaction, walletBalance)))
                .flatMap(providerGateway::createBankTransaction)
                .flatMap(providerTransactionResponse -> walletGateway.createWalletTransaction(transaction.getAmount(), transaction.getUserId(), true)
                        .flatMap(walletTransaction -> transactionHistoryRepository.saveTransaction(buildTransactionHistory(transaction, providerTransactionResponse))));
    }

    private Mono<Transaction> transactionFieldsValidation(Transaction transaction) {

        String additionalInfo = "The body is missing 1 or more mandatory fields. Body: " + transaction;

        return isNull(transaction.getAmount()) || isNull(transaction.getUserId()) || isNull(transaction.getBankAccountId())
                ? Mono.error(new BusinessException(BusinessException.Type.MANDATORY_FIELDS_MISSING, additionalInfo))
                : Mono.just(transaction);
    }

    private Mono<ProviderTransaction> balanceValidation(ProviderTransaction providerTransaction, WalletBalance walletBalance) {

        String additionalInfo = "Current Wallet Balance: " + walletBalance.getBalance() + " Balance required: " + providerTransaction.getAmount();

        return walletBalance.getBalance().compareTo(providerTransaction.getAmount()) >= 0
                ? Mono.just(providerTransaction)
                : Mono.error(new BusinessException(BusinessException.Type.INSUFFICIENT_FUNDS, additionalInfo));
    }

    private Mono<ProviderTransaction> buildProviderTransaction(Transaction transaction, User user) {

        BigDecimal amountWithFeeApplied = transaction.getAmount().subtract(transaction.getAmount().multiply(onTopFee));

        DecimalFormat df = new DecimalFormat("0.00%");

        log.info("Applying a " + df.format(onTopFee) + " fee to the amount.");

        log.info("Amount to receive after fee is: " + amountWithFeeApplied);

        return findUserBankAccount(transaction, user)
                .map(userBankAccount -> ProviderTransaction.builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .userBankAccount(userBankAccount)
                        .amount(amountWithFeeApplied)
                        .build());
    }

    private Mono<UserBankAccount> findUserBankAccount(Transaction transaction, User user) {
        return Mono.justOrEmpty(user.getUserBankAccountList()
                        .stream()
                        .filter(userBankAccount -> userBankAccount.getUserBankAccountId().equals(transaction.getBankAccountId()))
                        .findAny())
                .switchIfEmpty(Mono.error(new BusinessException(BusinessException.Type.NO_MATCHING_BANK_FOUND, transaction + " " + user)));
    }

    private TransactionHistory buildTransactionHistory(Transaction transaction,
                                                       ProviderTransactionResponse providerTransactionResponse) {
        return TransactionHistory.builder()
                .userId(transaction.getUserId())
                .amountSent(transaction.getAmount().toString())
                .userBankAccountId(transaction.getBankAccountId())
                .transactionStatus("SENT")
                .request(providerTransactionResponse.getRequest())
                .response(providerTransactionResponse.getResponse())
                .createdAt(new Date())
                .build();
    }


}

