package ru.dipech.listeneng.config;

import ru.dipech.listeneng.exception.ApplicationException;
import ru.dipech.listeneng.entity.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiResponse<Object>> handle(Exception exception) {
        ApiResponse<Object> response = ApiResponse.error(exception);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
