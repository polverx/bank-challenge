package co.com.evermore.dto;

import co.com.evermore.model.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    private BigInteger userId;
    private String bankAccountId;
    private BigDecimal amount;


    public Transaction getTransaction() {
        return Transaction.builder()
                .userId(userId)
                .bankAccountId(bankAccountId)
                .amount(amount)
                .build();
    }

}
