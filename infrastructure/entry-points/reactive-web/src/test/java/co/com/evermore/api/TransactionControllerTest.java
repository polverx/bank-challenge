package co.com.evermore.api;

import co.com.evermore.dto.TransactionDto;
import co.com.evermore.dto.TransactionResponseDto;
import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import co.com.evermore.usecase.TransactionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionUseCase transactionUseCase;

    @Mock
    private TransactionHistoryUseCase transactionHistoryUseCase;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void testGetAllTransactions() {
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .userId(BigInteger.valueOf(1))
                .amountSent("100")
                .userBankAccountId("1234")
                .transactionStatus("SUCCESS")
                .request("Transaction request")
                .response("Transaction response")
                .createdAt(new Date())
                .build();

        when(transactionHistoryUseCase.getAllTransactions()).thenReturn(Flux.just(transactionHistory));

        Flux<TransactionHistory> transactionHistoryFlux = transactionController.getAll();

        transactionHistoryFlux.subscribe(transaction -> {
            assert transaction.getUserId().equals(BigInteger.valueOf(1));
            assert transaction.getAmountSent().equals("100");
            assert transaction.getUserBankAccountId().equals("1234");
            assert transaction.getTransactionStatus().equals("SUCCESS");
            assert transaction.getRequest().equals("Transaction request");
            assert transaction.getResponse().equals("Transaction response");
            assert transaction.getCreatedAt() != null;
        });
    }

    @Test
    public void testCreateNewTransaction() {

        TransactionDto transactionDto = TransactionDto.builder()
                .userId(BigInteger.valueOf(1))
                .bankAccountId("1234")
                .amount(BigDecimal.valueOf(100))
                .build();

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .userId(BigInteger.valueOf(1))
                .amountSent("100")
                .userBankAccountId("1234")
                .transactionStatus("SUCCESS")
                .request("Transaction request")
                .response("Transaction response")
                .createdAt(new Date())
                .build();

        when(transactionUseCase.createNewTransaction(any()))
                .thenReturn(Mono.just(transactionHistory));

        Mono<ResponseEntity<TransactionResponseDto>> createdTransactionMono = transactionController.createTransaction(transactionDto);

        createdTransactionMono.subscribe(createdTransaction -> {
            assert createdTransaction.getBody().getResult().getUserId().equals(BigInteger.valueOf(1));
            assert createdTransaction.getBody().getResult().getAmountSent().equals("100");
            assert createdTransaction.getBody().getResult().getUserBankAccountId().equals("1234");
            assert createdTransaction.getBody().getResult().getTransactionStatus().equals("SUCCESS");
            assert createdTransaction.getBody().getResult().getRequest().equals("Transaction request");
            assert createdTransaction.getBody().getResult().getResponse().equals("Transaction response");
            assert createdTransaction.getBody().getResult().getCreatedAt() != null;
        });
    }
}
