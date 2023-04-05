package co.com.evermore.usecase;

import co.com.evermore.model.user.TransactionHistory;
import co.com.evermore.model.user.gateways.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;


@RequiredArgsConstructor
public class TransactionHistoryUseCase {
    private final TransactionHistoryRepository transactionHistoryRepository;

    public Flux<TransactionHistory> getAllTransactions() {
        return transactionHistoryRepository.getAllTransactions();
    }
}
