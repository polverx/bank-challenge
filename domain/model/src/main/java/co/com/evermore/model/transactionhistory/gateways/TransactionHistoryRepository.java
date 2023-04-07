package co.com.evermore.model.transactionhistory.gateways;

import co.com.evermore.model.transactionhistory.TransactionHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionHistoryRepository {
    Flux<TransactionHistory> getAllTransactions();

    Mono<TransactionHistory> saveTransaction(TransactionHistory transactionHistory);
}
