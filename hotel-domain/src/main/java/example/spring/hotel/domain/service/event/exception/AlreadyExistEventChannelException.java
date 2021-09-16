package example.spring.hotel.domain.service.event.exception;

public class AlreadyExistEventChannelException extends Exception {
    public AlreadyExistEventChannelException(String message) {
        super(message);
    }

    public AlreadyExistEventChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}
