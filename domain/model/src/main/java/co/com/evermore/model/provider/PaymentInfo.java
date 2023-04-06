package co.com.evermore.model.provider;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class PaymentInfo {
    private final BigDecimal amount;
    private final String providerTransactionId;

}
