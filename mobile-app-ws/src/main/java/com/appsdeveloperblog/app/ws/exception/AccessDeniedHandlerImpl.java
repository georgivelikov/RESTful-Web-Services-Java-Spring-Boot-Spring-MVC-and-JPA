package com.appsdeveloperblog.app.ws.exception;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
	    throws IOException, ServletException {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
	String dateStr = df.format(new Date());
	ExceptionMessageRest exception = new ExceptionMessageRest(dateStr, getPath(request), getMethod(request),
		ex.getMessage());

	ObjectMapper mapper = new ObjectMapper();

	response.setStatus(HttpStatus.FORBIDDEN.value());
	response.setContentType("application/json");
	response.getWriter().write(mapper.writeValueAsString(exception));
    }

    private String getPath(HttpServletRequest request) {
	return request.getRequestURI().toString();
    }

    private String getMethod(HttpServletRequest request) {
	return request.getMethod().toString();
    }
}
