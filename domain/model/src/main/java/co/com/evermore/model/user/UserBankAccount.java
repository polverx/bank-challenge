package co.com.evermore.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserBankAccount {
    private final String userBankAccountId;
    private final String bankName;
    private final String accountNumber;
    private final String currency;
    private final String routingNumber;
}
