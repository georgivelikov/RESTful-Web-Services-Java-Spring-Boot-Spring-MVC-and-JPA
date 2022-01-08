package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.appsdeveloperblog.app.ws.exception.ExceptionMessageRest;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {
	String dateStr = Utils.getDateString(new Date());
	String path = request.getRequestURI().toString();
	String method = request.getMethod().toString();
	String addtionalMessage = "Wrong password or incorrect JWT";
	ExceptionMessageRest exception = new ExceptionMessageRest(dateStr, path, method, authException.getMessage(),
		addtionalMessage);

	ObjectMapper mapper = new ObjectMapper();

	response.setStatus(HttpStatus.UNAUTHORIZED.value());
	response.setCharacterEncoding("utf-8");
	response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	response.getWriter().write(mapper.writeValueAsString(exception));
    }

}
