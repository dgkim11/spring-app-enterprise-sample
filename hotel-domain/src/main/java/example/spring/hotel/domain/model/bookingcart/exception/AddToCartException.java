package example.spring.hotel.domain.model.bookingcart.exception;

public class AddToCartException extends Exception {
    public AddToCartException(String message) {
        super(message);
    }

    public AddToCartException(String message, Throwable cause) {
        super(message, cause);
    }
}
