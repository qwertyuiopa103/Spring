package ispan.user.exception;

public class AccountPasswordChangeException extends RuntimeException {
    public AccountPasswordChangeException(String message) {
        super(message);
    }
}
