package co.com.evermore.consumer.ontopmock.dto;

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
public class ProviderTransactionRequestDto {
    @JsonProperty("source")
    private ProviderTransactionSourceDto providerTransactionSourceDto;
    @JsonProperty("destination")
    private ProviderTransactionDestinationDto providerTransactionDestinationDto;
    private BigDecimal amount;
}
