package co.com.evermore.mongo.transactionhistory.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(value = "transactionHistory")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionHistoryData {
    @Id
    private String id;
    private Integer userId;
    private String bankAccountId;
    private String amountSent;
    private String userBankAccountId;
    private String transactionStatus;
    private String request;
    private String response;
    private Date createdAt;
}
