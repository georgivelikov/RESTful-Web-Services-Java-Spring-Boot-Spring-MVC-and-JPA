package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException ex, WebRequest request) {
	ExceptionMessageRest exception = new ExceptionMessageRest(new Date(), ex.getMessage());
	return new ResponseEntity<>(exception, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
	ExceptionMessageRest exception = new ExceptionMessageRest(new Date(), ex.getMessage());
	return new ResponseEntity<>(exception, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
