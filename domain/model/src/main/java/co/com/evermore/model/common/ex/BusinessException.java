package co.com.evermore.model.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {
        INSUFFICIENT_FUNDS("Insufficient funds."),
        MANDATORY_FIELDS_MISSING("Mandatory fields where not found."),
        NO_MATCHING_BANK_FOUND("User has no bank matching the bank id"),
        USER_NOT_FOUND("The user was not found on the database."),
        SERVICE_EXCEPTION("A service exception occurred.");

        private final String message;

        public String getMessage() {
            return message;
        }

        public BusinessException build(String errorInfo) {
            return new BusinessException(this, errorInfo);
        }

        public Supplier<Throwable> defer() {
            return () -> new BusinessException(this, "");
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final Type type;

    public BusinessException(Type type, String exceptionInfo) {
        super(type.message + " " + exceptionInfo);
        this.type = type;
    }

    @Override
    public String getCode() {
        return type.name();
    }

    public Type getType() {
        return type;
    }


}
