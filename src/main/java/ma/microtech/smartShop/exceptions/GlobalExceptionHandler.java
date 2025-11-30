package ma.microtech.smartShop.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import ma.microtech.smartShop.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(ex.getMessage())
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message(ex.getMessage())
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler({BadRequestException.class, BusinessRuleException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, ServletWebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .orElse("Validation error");
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(message)
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message("Data constraint violation: " + ex.getMostSpecificCause().getMessage())
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, ServletWebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path(request.getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
