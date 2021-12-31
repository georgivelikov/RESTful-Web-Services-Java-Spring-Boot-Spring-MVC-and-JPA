package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto user) throws RestApiException;

    public UserDto getUser(String email) throws RestApiException;

    public UserDto getUserById(String id) throws RestApiException;

    public UserDto updateUser(String id, UserDto user) throws RestApiException;

    public void deleteUser(String id) throws RestApiException;

    public List<UserDto> getUsers(int page, int limit) throws RestApiException;
}
