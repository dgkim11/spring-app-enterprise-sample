package example.spring.hotel.domain.model.exception;

public class EntityValidationException extends RuntimeException  {
    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
