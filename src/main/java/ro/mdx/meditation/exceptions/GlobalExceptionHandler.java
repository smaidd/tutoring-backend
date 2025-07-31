package ro.mdx.meditation.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<ErrorResponse> handleException(GlobalException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }
}