package es.nextdigital.demo.exception;


import es.nextdigital.demo.model.response.OperationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<OperationResponse> handleInvalid(CustomException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new OperationResponse(ex.getMessage()));
    }
}
