package com.appsdeveloperblog.app.ws.exception;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException ex, WebRequest request) {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
	String dateStr = df.format(new Date());
	ExceptionMessageRest exception = new ExceptionMessageRest(dateStr, getPath(request), getMethod(request),
		ex.getMessage(), ex.getSourceExceptionMessage());
	return new ResponseEntity<>(exception, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
	String dateStr = df.format(new Date());
	ExceptionMessageRest exception = new ExceptionMessageRest(dateStr, getPath(request), getMethod(request),
		ex.getMessage());
	return new ResponseEntity<>(exception, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getPath(WebRequest request) {
	// TODO must check when there is param in the path
	return ((ServletWebRequest) request).getRequest().getRequestURI().toString();
    }

    private String getMethod(WebRequest request) {
	return ((ServletWebRequest) request).getHttpMethod().toString();
    }
}
