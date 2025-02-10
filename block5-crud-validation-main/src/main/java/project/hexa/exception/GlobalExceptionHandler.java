package project.hexa.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handling specific exceptions


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomError> handleEntityNotFoundException(EntityNotFoundException ex) {
        CustomError error = new CustomError(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Create a friendly message based on validation errors
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()))
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation error occurred");

        // Create the custom error object
        CustomError error = new CustomError(new Date(), HttpStatus.UNPROCESSABLE_ENTITY.value(), errorMessage);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CustomError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        CustomError error = new CustomError(new Date(), HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
//    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomError> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        CustomError error = new CustomError(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(WrongOutputTypeException.class)
    public ResponseEntity<CustomError> handleWrongOutputTypeException(WrongOutputTypeException ex) {
        CustomError error = new CustomError(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomError> handleRuntimeException(RuntimeException ex) {
        CustomError error = new CustomError(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handling generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
    }
}
