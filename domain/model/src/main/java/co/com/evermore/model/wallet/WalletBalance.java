package co.com.evermore.model.wallet;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder(toBuilder = true)
public class WalletBalance {
    private final BigInteger userId;
    private final BigDecimal balance;
}
