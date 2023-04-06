package co.com.evermore.model.user.gateways;

import co.com.evermore.model.user.User;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UserRepository {
    Mono<User> findUserById(BigInteger userId);
}
