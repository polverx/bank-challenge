package co.com.evermore.mongo.user;

import co.com.evermore.mongo.user.data.UserData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserDataRepository extends ReactiveMongoRepository<UserData, String> {
}
