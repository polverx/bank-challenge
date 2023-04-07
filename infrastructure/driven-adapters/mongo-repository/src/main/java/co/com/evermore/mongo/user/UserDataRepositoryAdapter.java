package co.com.evermore.mongo.user;

import co.com.evermore.model.common.ex.BusinessException;
import co.com.evermore.model.user.User;
import co.com.evermore.model.user.gateways.UserRepository;
import co.com.evermore.mongo.helper.AdapterOperations;
import co.com.evermore.mongo.user.data.UserData;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Log
@Component
@Repository
public class UserDataRepositoryAdapter
        extends AdapterOperations<User, UserData, String, UserDataRepository>
        implements UserRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    private static final String USER_ID = "userId";

    @Autowired
    public UserDataRepositoryAdapter(
            UserDataRepository repository,
            ObjectMapper mapper,
            ReactiveMongoTemplate mongoTemplate
    ) {
        super(repository, mapper, userData -> mapper.mapBuilder(userData, User.UserBuilder.class)
                .build());
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<User> findUserById(BigInteger userId) {

        String additionalInfo = "User id: " + userId;

        return mongoTemplate.findOne(buildUserQuery(userId), UserData.class)
                .map(UserData::buildUserEntity)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessException.Type.USER_NOT_FOUND, additionalInfo)));
    }


    private Query buildUserQuery(BigInteger userId) {

        final Query query = new Query();

        return query.addCriteria(Criteria.where(USER_ID).is(userId.intValue()));
    }

}
