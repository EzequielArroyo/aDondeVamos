package com.github.ezequielarroyo.postservice.exceptions;

import com.github.ezequielarroyo.domain.commonexceptions.ErrorResponse;
import com.github.ezequielarroyo.domain.commonexceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "com.github.ezequielarroyo.domain.userservice.controllers")
@Primary
public class UserSnapshotExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(UserSnapshotNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(PostNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
