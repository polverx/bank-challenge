package co.com.evermore.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TransactionHistory {
    private final String userId;
    private final String amountSent;
}
