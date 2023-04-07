package co.com.evermore.consumer.ontopmock.dto;

import co.com.evermore.model.provider.PaymentInfo;
import co.com.evermore.model.provider.ProviderTransactionResponse;
import co.com.evermore.model.provider.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class ProviderTransactionResponseDto {
    @JsonProperty("requestInfo")
    private RequestInfoDto requestInfoDto;
    @JsonProperty("paymentInfo")
    private PaymentInfoDto paymentInfoDto;

    public Mono<ProviderTransactionResponse> toMonoProviderTransaction() {
        return Mono.just(
                ProviderTransactionResponse.builder()
                        .requestInfo(requestInfoDto.buildRequestInfo())
                        .paymentInfo(paymentInfoDto.buildPaymentInfo())
                        .build());
    }

}
