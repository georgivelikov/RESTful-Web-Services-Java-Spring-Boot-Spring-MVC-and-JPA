package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.appsdeveloperblog.app.ws.exception.ExceptionMessageRest;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
	    AccessDeniedException accessDeniedException) throws IOException, ServletException {
	String dateStr = Utils.getDateString(new Date());
	String path = request.getRequestURI().toString();
	String method = request.getMethod().toString();

	ExceptionMessageRest exception = new ExceptionMessageRest(dateStr, path, method,
		accessDeniedException.getMessage());

	ObjectMapper mapper = new ObjectMapper();

	response.setStatus(HttpStatus.FORBIDDEN.value());
	response.setCharacterEncoding("utf-8");
	response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	response.getWriter().write(mapper.writeValueAsString(exception));
    }

}
