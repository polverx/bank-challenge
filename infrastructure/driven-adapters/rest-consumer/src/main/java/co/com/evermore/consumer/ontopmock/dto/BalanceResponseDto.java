package co.com.evermore.consumer.ontopmock.dto;

import co.com.evermore.model.wallet.WalletBalance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class BalanceResponseDto {
    private BigDecimal balance;
    private BigInteger userId;

    public WalletBalance getWalletBalance() {
        return WalletBalance.builder()
                .balance(balance)
                .userId(userId)
                .build();
    }

}
