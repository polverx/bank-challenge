package co.com.evermore.api;

import co.com.evermore.dto.TransactionDto;
import co.com.evermore.dto.TransactionResponseDto;
import co.com.evermore.model.common.ex.BusinessException;
import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.usecase.TransactionHistoryUseCase;
import co.com.evermore.usecase.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
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
    public Mono<ResponseEntity<TransactionResponseDto>> createTransaction(@RequestBody TransactionDto body) {
        return transactionUseCase.createNewTransaction(body.getTransaction())
                .map(transactionHistory -> ResponseEntity
                        .ok()
                        .body(TransactionResponseDto.builder()
                                .result(transactionHistory)
                                .build()))
                .onErrorResume(BusinessException.class, TransactionController::handleBusinessException)
                .onErrorResume(Throwable.class, TransactionController::handleGeneralException);
    }

    private static Mono<ResponseEntity<TransactionResponseDto>> handleGeneralException(Throwable e) {
        log.error("An error occurred while processing the transaction: " + e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    private static Mono<ResponseEntity<TransactionResponseDto>> handleBusinessException(BusinessException e) {
        log.error("An error occurred while processing the transaction: " + e.getMessage());
        return Mono.just(ResponseEntity
                .badRequest()
                .body(TransactionResponseDto.builder()
                        .error(e.getMessage())
                        .build()));
    }

}
