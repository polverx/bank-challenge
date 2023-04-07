package co.com.evermore.model.provider;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class RequestInfo {
    private final String status;
    private final String error;
}
