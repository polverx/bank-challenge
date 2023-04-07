package co.com.evermore.model.transactionhistory;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder(toBuilder = true)
public class TransactionHistory {
    private final BigInteger userId;
    private final String amountSent;
    private final String userBankAccountId;
    private final String transactionStatus;
    private final String request;
    private final String response;
    private final Date createdAt;
}
