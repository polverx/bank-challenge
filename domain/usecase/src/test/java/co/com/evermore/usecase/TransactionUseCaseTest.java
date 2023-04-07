package co.com.evermore.usecase;


import co.com.evermore.model.common.ex.BusinessException;
import co.com.evermore.model.provider.PaymentInfo;
import co.com.evermore.model.provider.ProviderTransactionResponse;
import co.com.evermore.model.provider.RequestInfo;
import co.com.evermore.model.provider.gateways.ProviderTransactionGateway;
import co.com.evermore.model.transaction.Transaction;
import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.model.transactionhistory.gateways.TransactionHistoryRepository;
import co.com.evermore.model.user.User;
import co.com.evermore.model.user.UserBankAccount;
import co.com.evermore.model.user.gateways.UserRepository;
import co.com.evermore.model.wallet.WalletBalance;
import co.com.evermore.model.wallet.WalletTransaction;
import co.com.evermore.model.wallet.gateways.WalletGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TransactionUseCaseTest {

    @InjectMocks
    private TransactionUseCase useCase;

    @Mock
    private WalletGateway walletGateway;

    @Mock
    private ProviderTransactionGateway providerGateway;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    private Transaction transaction;
    private User user;

    private WalletBalance walletBalance;

    private ProviderTransactionResponse providerTransactionResponse;

    private WalletTransaction walletTransaction;

    private TransactionHistory transactionHistory;


    private static final String TEST_STRING = "1";
    private static final BigDecimal TEST_BIG_DECIMAL = BigDecimal.ONE;
    private static final BigInteger TEST_BIG_INTEGER = BigInteger.ONE;


    @BeforeEach
    public void setup() {

        ReflectionTestUtils.setField(useCase, "onTopFee", new BigDecimal("0.1"));

        Date mockDate = new Date();

        transaction = Transaction.builder()
                .userId(TEST_BIG_INTEGER)
                .bankAccountId(TEST_STRING)
                .amount(TEST_BIG_DECIMAL)
                .build();

        UserBankAccount userBankAccount = UserBankAccount.builder()
                .userBankAccountId(TEST_STRING)
                .bankName(TEST_STRING)
                .accountNumber(TEST_STRING)
                .currency(TEST_STRING)
                .routingNumber(TEST_STRING)
                .build();

        user = User.builder()
                .userId(TEST_BIG_INTEGER)
                .firstName(TEST_STRING)
                .lastName(TEST_STRING)
                .idType(TEST_STRING)
                .idNumber(TEST_STRING)
                .userBankAccountList(List.of(userBankAccount))
                .build();

        walletBalance = WalletBalance.builder()
                .userId(TEST_BIG_INTEGER)
                .balance(new BigDecimal(2000))
                .build();

        providerTransactionResponse = ProviderTransactionResponse.builder()
                .requestInfo(RequestInfo.builder()
                        .status(TEST_STRING)
                        .error(TEST_STRING)
                        .build())
                .paymentInfo(PaymentInfo.builder()
                        .amount(TEST_BIG_DECIMAL)
                        .providerTransactionId(TEST_STRING)
                        .build())
                .request("{request}")
                .response("{response}")
                .build();

        walletTransaction = WalletTransaction.builder()
                .userId(TEST_BIG_INTEGER)
                .amount(TEST_BIG_DECIMAL)
                .walletTransactionId(TEST_BIG_INTEGER)
                .build();

        transactionHistory = TransactionHistory.builder()
                .userId(TEST_BIG_INTEGER)
                .amountSent(TEST_STRING)
                .userBankAccountId(TEST_STRING)
                .transactionStatus(TEST_STRING)
                .request("{request}")
                .response("{response}")
                .createdAt(mockDate)
                .build();


    }

    @Test
    public void testCreateNewTransaction() {

        Mockito.when(userRepository.findUserById(any()))
                .thenReturn(Mono.just(user));

        Mockito.when(walletGateway.getUserBalance(any()))
                .thenReturn(Mono.just(walletBalance));

        Mockito.when(providerGateway.createBankTransaction(any()))
                .thenReturn(Mono.just(providerTransactionResponse));

        Mockito.when(walletGateway.createWalletTransaction(any(), any(), any()))
                .thenReturn(Mono.just(walletTransaction));

        Mockito.when(transactionHistoryRepository.saveTransaction(any()))
                .thenReturn(Mono.just(transactionHistory));


        Mono<TransactionHistory> createTransaction = useCase.createNewTransaction(transaction);

        StepVerifier.create(createTransaction)
                .expectNext(transactionHistory)
                .expectComplete()
                .verify();


    }


    @Test
    void createNewTransactionMissingFields() {

        Mono<TransactionHistory> createTransaction = useCase.createNewTransaction(Transaction.builder()
                        .userId(null)
                .build());


        StepVerifier.create(createTransaction)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException
                        && ((BusinessException) throwable).getType() == BusinessException.Type.MANDATORY_FIELDS_MISSING)
                .verify();
    }


    @Test
    void createNewTransactionInsufficientFunds() {

        Mockito.when(userRepository.findUserById(any()))
                .thenReturn(Mono.just(user));

        Mockito.when(walletGateway.getUserBalance(any()))
                .thenReturn(Mono.just(walletBalance.toBuilder()
                        .balance(new BigDecimal(500))
                        .build()));

        Mono<TransactionHistory> createTransaction = useCase.createNewTransaction(transaction.toBuilder()
                        .amount(new BigDecimal(5000))
                .build());


        StepVerifier.create(createTransaction)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException
                        && ((BusinessException) throwable).getType() == BusinessException.Type.INSUFFICIENT_FUNDS)
                .verify();
    }




}