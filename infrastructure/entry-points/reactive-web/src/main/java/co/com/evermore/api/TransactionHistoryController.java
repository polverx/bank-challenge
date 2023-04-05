package co.com.evermore.api;

import co.com.evermore.model.user.TransactionHistory;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TransactionHistoryController {
    private final TransactionHistoryUseCase useCase;

//    @GetMapping(path = "/transactions/{id}")
//    public Mono<TransactionHistory> getTransactionHistoryById(@PathVariable String id) {
//        return useCase.getTransactionById(id);
//    }

    @GetMapping(path = "/transactions")
    public Flux<TransactionHistory> getAll() {
        return useCase.getAllTransactions();
    }
}
