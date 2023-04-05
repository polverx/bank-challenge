package co.com.evermore.model.user.gateways;

import co.com.evermore.model.user.TransactionHistory;
import reactor.core.publisher.Flux;

public interface TransactionHistoryRepository {
    Flux<TransactionHistory> getAllTransactions();
}
