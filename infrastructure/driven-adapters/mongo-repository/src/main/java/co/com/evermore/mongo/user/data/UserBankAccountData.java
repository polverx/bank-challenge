package co.com.evermore.mongo.user.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserBankAccountData {
    private String userBankAccountId;
    private String bankName;
    private String accountNumber;
    private String currency;
    private String routingNumber;

}
