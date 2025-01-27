package ispan.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<?> handleAccountLockedException(AccountLockedException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<?> handleAccountDisabledException(AccountDisabledException ex) {
        // 例如回傳 403(Forbidden) 或 423(Locked)，取決於你的需求
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<?> handleAccountNotVerifiedException(AccountNotVerifiedException ex) {
        // 亦或是 403 或 401
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    // 可添加其他異常處理
    
    @ExceptionHandler(AccountPasswordChangeException.class)
    public ResponseEntity<?> handleAccountPasswordChangeException(AccountPasswordChangeException ex) {
        // 亦或是 403 或 401
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    // 可添加其他異常處理
    
    @ExceptionHandler(SoftDeleteException.class)
    public ResponseEntity<?> handleSoftDeleteException(SoftDeleteException ex) {
        // 亦或是 403 或 401
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}