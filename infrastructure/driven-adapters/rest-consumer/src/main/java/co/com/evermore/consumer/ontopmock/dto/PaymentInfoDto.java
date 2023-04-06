package co.com.evermore.consumer.ontopmock.dto;

import co.com.evermore.model.provider.PaymentInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class PaymentInfoDto {
    private BigDecimal amount;
    @JsonProperty("id")
    private String providerTransactionId;

    public PaymentInfo buildPaymentInfo() {
        return PaymentInfo.builder()
                .amount(amount)
                .providerTransactionId(providerTransactionId)
                .build();
    }

}
