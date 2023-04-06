package co.com.evermore.mongo.user.data;

import co.com.evermore.model.user.User;
import co.com.evermore.model.user.UserBankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Document(value = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserData {

    @Id
    private String id;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String idType;
    private String idNumber;
    @Field("bankAccounts")
    private List<UserBankAccountData> userBankAccountDataList;

    public User buildUserEntity() {
        return User.builder()
                .userId(BigInteger.valueOf(userId))
                .firstName(firstName)
                .lastName(lastName)
                .idType(idType)
                .idNumber(idNumber)
                .userBankAccountList(getUserBankAccounts(userBankAccountDataList))
                .build();
    }

    private List<UserBankAccount> getUserBankAccounts(List<UserBankAccountData> userBankAccountDataList) {
        return userBankAccountDataList
                .stream()
                .map(userBankAccountData -> UserBankAccount.builder()
                        .userBankAccountId(userBankAccountData.getUserBankAccountId())
                        .bankName(userBankAccountData.getBankName())
                        .accountNumber(userBankAccountData.getAccountNumber())
                        .currency(userBankAccountData.getCurrency())
                        .routingNumber(userBankAccountData.getRoutingNumber())
                        .build())
                .collect(Collectors.toList());
    }

}
