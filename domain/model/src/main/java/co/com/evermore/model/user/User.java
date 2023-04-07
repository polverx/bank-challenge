package co.com.evermore.model.user;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class User {
    private final BigInteger userId;
    private final String firstName;
    private final String lastName;
    private final String idType;
    private final String idNumber;
    private final List<UserBankAccount> userBankAccountList;
}
