package co.com.evermore.consumer.ex;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ServiceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}