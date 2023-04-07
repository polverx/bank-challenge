package co.com.evermore.model.wallet.gateways;

import co.com.evermore.model.wallet.WalletBalance;
import co.com.evermore.model.wallet.WalletTransaction;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface WalletGateway {
    Mono<WalletBalance> getUserBalance(BigInteger userId);

    Mono<WalletTransaction> createWalletTransaction(BigDecimal amount, BigInteger userId, Boolean withdraw);
}
