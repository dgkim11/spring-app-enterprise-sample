package example.spring.hotel.domain.service.event.exception;

public class NotExistEventChannelException extends Exception {
    public NotExistEventChannelException(String message) {
        super(message);
    }

    public NotExistEventChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}
