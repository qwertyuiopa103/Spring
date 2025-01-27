package ispan.user.exception;

public class SoftDeleteException extends RuntimeException {
    public SoftDeleteException (String message) {
        super(message);
    }
}
