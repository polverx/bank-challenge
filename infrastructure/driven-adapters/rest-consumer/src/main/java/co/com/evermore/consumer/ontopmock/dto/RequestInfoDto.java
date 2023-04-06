package co.com.evermore.consumer.ontopmock.dto;

import co.com.evermore.model.provider.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class RequestInfoDto {
    private String status;
    private String error;

    public RequestInfo buildRequestInfo() {
        return RequestInfo.builder()
                .status(status)
                .error(error)
                .build();
    }

}
