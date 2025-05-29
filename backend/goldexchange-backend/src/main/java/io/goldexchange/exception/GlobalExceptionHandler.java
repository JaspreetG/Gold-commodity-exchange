package io.goldexchange.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidSessionException.class)
    public ResponseEntity<String> handleInvalidSession(InvalidSessionException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }
}
