package co.com.evermore.model.provider;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ProviderTransactionResponse {
    private final RequestInfo requestInfo;
    private final PaymentInfo paymentInfo;
    private final String request;
    private final String response;

}
