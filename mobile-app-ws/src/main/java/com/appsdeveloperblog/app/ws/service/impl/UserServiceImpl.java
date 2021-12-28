package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptpasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	UserEntity userEntity = userRepository.findByEmail(email);

	if (userEntity == null) {
	    throw new UsernameNotFoundException(email);
	}

	return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
	UserEntity userEntity = new UserEntity();
	BeanUtils.copyProperties(userDto, userEntity);

	// For now just mock this props:
	userEntity.setUserId(utils.generateUserId(30));
	userEntity.setEncryptedPassword(bCryptpasswordEncoder.encode(userDto.getPassword()));

	UserEntity storedUserEntity = userRepository.save(userEntity);
	UserDto returnedUserDto = new UserDto();
	BeanUtils.copyProperties(storedUserEntity, returnedUserDto);

	return returnedUserDto;
    }

    @Override
    public UserDto getUser(String email) {
	UserEntity userEntity = userRepository.findByEmail(email);
	if (userEntity == null) {
	    throw new UsernameNotFoundException(email);
	}

	UserDto returnValue = new UserDto();
	BeanUtils.copyProperties(userEntity, returnValue);

	return returnValue;
    }

    @Override
    public UserDto getUserById(String id) {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new UsernameNotFoundException(id);
	}

	UserDto returnValue = new UserDto();
	BeanUtils.copyProperties(userEntity, returnValue);

	return returnValue;
    }

}
