package co.com.evermore.model.provider;

import co.com.evermore.model.user.UserBankAccount;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class ProviderTransaction {
    private final String firstName;
    private final String lastName;
    private final UserBankAccount userBankAccount;
    private final BigDecimal amount;

}
