package co.com.evermore.mongo.transactionhistory;

import co.com.evermore.model.user.TransactionHistory;
import co.com.evermore.model.user.gateways.TransactionHistoryRepository;
import co.com.evermore.mongo.helper.AdapterOperations;
import co.com.evermore.mongo.transactionhistory.data.TransactionHistoryData;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
@Component
@Repository
public class TransactionHistoryDataRepositoryAdapter
        extends AdapterOperations<TransactionHistory, TransactionHistoryData, String, TransactionHistoryDataRepository>
        implements TransactionHistoryRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public TransactionHistoryDataRepositoryAdapter(
            TransactionHistoryDataRepository repository,
            ObjectMapper mapper,
            ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, transactionHistoryData -> mapper.mapBuilder(transactionHistoryData, TransactionHistory.TransactionHistoryBuilder.class)
                .build());

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<TransactionHistory> getAllTransactions() {
        return mongoTemplate.findAll(TransactionHistoryData.class)
                .flatMap(this::mapToDomain);
    }

    private Mono<TransactionHistory> mapToDomain(TransactionHistoryData transactionHistoryData) {
        return Mono.fromCallable(() -> toEntity(transactionHistoryData))
                .switchIfEmpty(Mono.empty());
    }
}
