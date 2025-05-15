package example.com.userservice.exception;

public abstract class BusinessLogicException  extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }
}
