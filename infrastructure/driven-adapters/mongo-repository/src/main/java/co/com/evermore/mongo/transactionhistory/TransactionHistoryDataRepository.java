package co.com.evermore.mongo.transactionhistory;

import co.com.evermore.mongo.transactionhistory.data.TransactionHistoryData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionHistoryDataRepository extends ReactiveMongoRepository<TransactionHistoryData, String> {
}
