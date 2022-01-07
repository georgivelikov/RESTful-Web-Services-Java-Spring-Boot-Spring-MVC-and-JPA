package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appsdeveloperblog.app.ws.SpringApplicationContext;
import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
	this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
	    throws AuthenticationException {

	try {
	    UserLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(),
		    UserLoginRequestModel.class);
	    return authenticationManager.authenticate(
		    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
	    Authentication auth) throws IOException, ServletException {

	String userName = ((UserPrincipal) auth.getPrincipal()).getUsername();
	Date expirationDate = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
	String token = Jwts.builder()
		.setSubject(userName)
		.setExpiration(expirationDate)
		.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
		.compact();

	UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
	UserDto userDto;
	try {
	    userDto = userService.getUserForLogin(userName);
	} catch (RestApiException e) {
	    throw new ServletException(e);
	}

	res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKER_PREFIX + token);
	res.addHeader("UserID", userDto.getUserId());
    }
}
