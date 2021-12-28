package com.appsdeveloperblog.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto user);

    public UserDto getUser(String email);

    public UserDto getUserById(String id);
}
