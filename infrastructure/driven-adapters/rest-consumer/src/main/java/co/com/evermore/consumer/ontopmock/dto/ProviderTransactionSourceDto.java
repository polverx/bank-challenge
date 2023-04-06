package co.com.evermore.consumer.ontopmock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class ProviderTransactionSourceDto {
    private String type;
    @JsonProperty("sourceInformation")
    private ProviderTransactionSourceInformationDto providerTransactionSourceInformationDto;
    @JsonProperty("account")
    private ProviderTransactionAccountDto providerTransactionAccountDto;


}
