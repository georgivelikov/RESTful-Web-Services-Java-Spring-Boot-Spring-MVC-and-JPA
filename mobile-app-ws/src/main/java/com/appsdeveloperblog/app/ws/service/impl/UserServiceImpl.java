package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.ExceptionMessages;
import com.appsdeveloperblog.app.ws.exception.RestApiException;
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
    public UserDto createUser(UserDto userDto) throws RestApiException {
	UserEntity userEntity = new UserEntity();
	BeanUtils.copyProperties(userDto, userEntity);

	// For now just mock this props:
	userEntity.setUserId(utils.generateUserId(30));
	userEntity.setEncryptedPassword(bCryptpasswordEncoder.encode(userDto.getPassword()));

	UserEntity storedUserEntity = null;
	try {
	    storedUserEntity = userRepository.save(userEntity);
	} catch (Exception ex) {
	    throw new RestApiException(ExceptionMessages.RECORD_ALREADY_EXISTS.getErrorMessage(), ex.getMessage());
	}

	UserDto returnedUserDto = new UserDto();
	BeanUtils.copyProperties(storedUserEntity, returnedUserDto);

	return returnedUserDto;
    }

    @Override
    public UserDto getUser(String email) throws RestApiException {
	UserEntity userEntity = userRepository.findByEmail(email);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	UserDto returnValue = new UserDto();
	BeanUtils.copyProperties(userEntity, returnValue);

	return returnValue;
    }

    @Override
    public UserDto getUserById(String id) throws RestApiException {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	UserDto returnValue = new UserDto();
	BeanUtils.copyProperties(userEntity, returnValue);

	return returnValue;
    }

    @Override
    public UserDto updateUser(String id, UserDto user) throws RestApiException {
	UserDto returnValue = new UserDto();
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	userEntity.setFirstName(user.getFirstName());
	userEntity.setLastName(user.getLastName());

	UserEntity updatedUserEntity = userRepository.save(userEntity);
	BeanUtils.copyProperties(updatedUserEntity, returnValue);

	return returnValue;
    }

    @Override
    public void deleteUser(String id) throws RestApiException {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) throws RestApiException {
	List<UserDto> returnValue = new ArrayList<>();
	Pageable pageableRequest = PageRequest.of(page, limit);
	Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
	List<UserEntity> users = userPage.getContent();

	for (UserEntity userEntity : users) {
	    UserDto userDto = new UserDto();
	    BeanUtils.copyProperties(userEntity, userDto);
	    returnValue.add(userDto);
	}

	return returnValue;
    }

}
