package com.slembers.alarmony.global.handler;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.util.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    //얘 뭐냐
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied exception occurred: {}", e.getMessage());
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.CREATED);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.OK);
    }



}
