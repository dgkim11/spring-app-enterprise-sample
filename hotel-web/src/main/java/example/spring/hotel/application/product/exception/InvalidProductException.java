package example.spring.hotel.application.product.exception;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException(String message) {
        super(message);
    }

    public InvalidProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
