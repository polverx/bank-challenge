package co.com.evermore.consumer.ontopmock.dto;

import co.com.evermore.model.wallet.WalletTransaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public class WalletTransactionDto {
    private BigInteger userId;
    private BigDecimal amount;
    private BigInteger walletTransactionId;

    public Mono<WalletTransaction> toMonoWalletTransaction() {
        return Mono.just(
                WalletTransaction.builder()
                        .userId(userId)
                        .amount(amount)
                        .walletTransactionId(walletTransactionId)
                        .build());
    }
}
