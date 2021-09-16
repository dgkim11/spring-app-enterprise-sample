package example.spring.hotel.domain.model.checkout.exception;

public class CheckoutException extends Exception  {

    public CheckoutException(String message) {
        super(message);
    }

    public CheckoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
