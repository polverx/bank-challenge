package co.com.evermore.model.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder(toBuilder = true)
public class Transaction {
    private final BigInteger userId;
    private final String bankAccountId;
    private final BigDecimal amount;

}
