package co.com.evermore.usecase;

import co.com.evermore.model.provider.ProviderTransaction;
import co.com.evermore.model.provider.gateways.ProviderTransactionGateway;
import co.com.evermore.model.transaction.Transaction;
import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.model.transactionhistory.gateways.TransactionHistoryRepository;
import co.com.evermore.model.user.gateways.UserRepository;
import co.com.evermore.model.wallet.WalletTransaction;
import co.com.evermore.model.wallet.gateways.WalletGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.Date;

@Log
@RequiredArgsConstructor
public class TransactionUseCase {

    private final WalletGateway walletGateway;
    private final ProviderTransactionGateway providerGateway;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;


    public Mono<TransactionHistory> createNewTransaction(Transaction transaction) {

        return walletGateway.getUserBalance(transaction.getUserId())
                .flatMap(walletBalance -> Mono.just(walletBalance.getBalance().compareTo(transaction.getAmount()) >= 0))
                .flatMap(aBoolean -> userRepository.findUserById(transaction.getUserId()))
                .flatMap(user -> walletGateway.createWalletTransaction(WalletTransaction.builder()
                                .userId(user.getUserId())
                                .amount(transaction.getAmount())
                                .build())
                        .flatMap(walletTransaction -> providerGateway.createProviderTransaction(ProviderTransaction.builder()
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .userBankAccount(user.getUserBankAccountList()
                                        .stream()
                                        .filter(userBankAccount -> userBankAccount.getUserBankAccountId().equals(transaction.getBankAccountId()))
                                        .findAny()
                                        .orElse(null))
                                        .amount(transaction.getAmount())
                                .build())))
                .flatMap(providerTransactionResponse -> transactionHistoryRepository.saveTransaction(TransactionHistory.builder()
                        .userId(transaction.getUserId())
                        .amountSent(transaction.getAmount().toString())
                        .createdAt(new Date())
                        .userBankAccountId(transaction.getBankAccountId())
                        .transactionStatus("SENT")
                        .request(providerTransactionResponse.getRequest())
                        .response(providerTransactionResponse.getResponse())
                        .build()));
    }


}

