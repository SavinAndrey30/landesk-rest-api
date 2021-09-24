package ru.t1.asavin.techSupportAutomation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = createApiError(HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
        ApiError apiError = createApiError(HttpStatus.CONFLICT, ex);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    ResponseEntity<Object> handleScreenshotFileIncorrect(ScreenshotFileIncorrectException ex) {
        ApiError apiError = createApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
        return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public ApiError createApiError(HttpStatus httpStatus, Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setStatus(httpStatus.value());
        apiError.setMessage(ex.getMessage());

        return apiError;
    }
}
