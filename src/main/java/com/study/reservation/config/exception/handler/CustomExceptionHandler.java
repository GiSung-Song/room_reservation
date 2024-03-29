package com.study.reservation.config.exception.handler;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    // Global Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler", e);

        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));

        return new ResponseEntity<>(errorResponse, ErrorCode.NOT_VALID_ERROR.getHttpStatus());
    }

    // Custom Exception
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        log.error("customExceptionHandler", e);

        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), String.valueOf(e.getErrorCode()));

        return new ResponseEntity<>(errorResponse, e.getErrorCode().getHttpStatus());
    }
}
