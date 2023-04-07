package co.com.evermore.usecase;

import co.com.evermore.model.transactionhistory.TransactionHistory;
import co.com.evermore.model.transactionhistory.gateways.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;


@Log
@RequiredArgsConstructor
public class TransactionHistoryUseCase {
    private final TransactionHistoryRepository transactionHistoryRepository;

    public Flux<TransactionHistory> getAllTransactions() {
        return transactionHistoryRepository.getAllTransactions();
    }
}
