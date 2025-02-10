package project.hexa.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.hexa.components.StringConstants.*;

@SpringBootTest
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    public static final String RUNTIME_ERROR ="Runtime error";
    public static final String UNEXPECTED_FAILTURE ="Unexpected failure";
    public static final String UNEXPECTED_FAILTURE_ERROR ="Unexpected error: Unexpected failure";
    public static final String FIELDNAME_NOT_NULL="Field 'fieldName': must not be null";
    public static final String OBJECTNAME="objectName";
    public static final String FIELDNAME="fieldName";
    public static final String NOT_NULL="must not be null";



    @Test
    void testHandleEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException(PERSON_ID_NOT_FOUND);

        ResponseEntity<CustomError> response = exceptionHandler.handleEntityNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(PERSON_ID_NOT_FOUND, response.getBody().getMessage());
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError(OBJECTNAME, FIELDNAME, NOT_NULL);

        when(exception.getBindingResult()).thenReturn(mock(BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<CustomError> response = exceptionHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains(FIELDNAME_NOT_NULL));
    }

    @Test
    void testHandleUnprocessableEntityException() {
        UnprocessableEntityException exception = new UnprocessableEntityException(PERSON_IS_PROFESOR);

        ResponseEntity<CustomError> response = exceptionHandler.handleUnprocessableEntityException(exception);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(PERSON_IS_PROFESOR, response.getBody().getMessage());
    }

    @Test
    void testHandleWrongOutputTypeException() {
        WrongOutputTypeException exception = new WrongOutputTypeException(WRONG_OUTPUT_TYPE);

        ResponseEntity<CustomError> response = exceptionHandler.handleWrongOutputTypeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(WRONG_OUTPUT_TYPE, response.getBody().getMessage());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException(RUNTIME_ERROR);

        ResponseEntity<CustomError> response = exceptionHandler.handleRuntimeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(RUNTIME_ERROR, response.getBody().getMessage());
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception(UNEXPECTED_FAILTURE);

        ResponseEntity<String> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(UNEXPECTED_FAILTURE_ERROR, response.getBody());
    }
}
