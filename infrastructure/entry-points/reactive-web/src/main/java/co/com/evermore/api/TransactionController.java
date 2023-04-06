package co.com.evermore.api;

import co.com.evermore.dto.TransactionDto;
import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import co.com.evermore.usecase.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    private final TransactionHistoryUseCase transactionHistoryUseCase;

    @GetMapping(path = "api/v1/transactions")
    public Flux<TransactionHistory> getAll() {
        return transactionHistoryUseCase.getAllTransactions();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "api/v1/transactions")
    public Mono<TransactionHistory> createTransaction(@RequestBody TransactionDto body) {
        return transactionUseCase.createNewTransaction(body.getTransaction());
    }

}
