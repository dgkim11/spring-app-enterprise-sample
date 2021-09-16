package example.spring.hotel.domain.model.payment.exception;

public class PaymentException extends Exception  {
    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
