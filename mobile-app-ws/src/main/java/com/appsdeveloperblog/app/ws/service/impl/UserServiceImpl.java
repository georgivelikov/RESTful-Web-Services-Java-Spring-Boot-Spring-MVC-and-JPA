package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.ExceptionMessages;
import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.io.entity.AuthorityEntity;
import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
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

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	UserEntity userEntity = userRepository.findByEmail(email);

	if (userEntity == null) {
	    throw new UsernameNotFoundException(email);
	}

	return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), getAuthorities(userEntity));
    }

    @Override
    public UserDto createUser(UserDto userDto) throws RestApiException {

	if (userRepository.findByEmail(userDto.getEmail()) != null) {
	    throw new RestApiException(ExceptionMessages.COULD_NOT_CREATE_RECORD.getErrorMessage(),
		    ExceptionMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
	}

	for (int i = 0; i < userDto.getAddresses().size(); i++) {
	    AddressDto addressDto = userDto.getAddresses().get(i);
	    addressDto.setUserDetails(userDto);
	    addressDto.setAddressId(utils.generatePublicId(30));
	    userDto.getAddresses().set(i, addressDto);
	}

	UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

	userEntity.setUserId(utils.generatePublicId(30));
	userEntity.setEncryptedPassword(bCryptpasswordEncoder.encode(userDto.getPassword()));

	UserEntity storedUserEntity = null;
	try {
	    storedUserEntity = userRepository.save(userEntity);
	} catch (Exception ex) {
	    throw new RestApiException(ExceptionMessages.COULD_NOT_CREATE_RECORD.getErrorMessage(), ex.getMessage());
	}

	UserDto returnValue = modelMapper.map(storedUserEntity, UserDto.class);

	return returnValue;
    }

    @Override
    public UserDto getUser(String email) throws RestApiException {
	UserEntity userEntity = userRepository.findByEmail(email);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserForLogin(String email) throws RestApiException {
	UserEntity userEntity = userRepository.findByEmail(email);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	UserDto userDto = new UserDto();
	// Model Mapper is not allowed here, but BeanUtils is enough
	BeanUtils.copyProperties(userEntity, userDto);
	return userDto;
    }

    @Override
    public UserDto getUserByUserId(String id) throws RestApiException {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(String id, UserDto user) throws RestApiException {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	userEntity.setFirstName(user.getFirstName());
	userEntity.setLastName(user.getLastName());
	UserEntity updatedUserEntity = userRepository.save(userEntity);

	return modelMapper.map(updatedUserEntity, UserDto.class);
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
	    UserDto userDto = modelMapper.map(userEntity, UserDto.class);
	    returnValue.add(userDto);
	}

	return returnValue;
    }

    @Override
    public UserDto updateUserJpql(String userId, UserDto userDto) throws RestApiException {
	String firstName = userDto.getFirstName();
	String lastName = userDto.getLastName();
	int result = userRepository.updateUserJpql(userId, firstName, lastName);

	// result == 0 means no rows are updated, therefore there is some problem
	if (result == 0) {
	    throw new RestApiException(ExceptionMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage());
	}

	return modelMapper.map(userRepository.findByUserId(userId), UserDto.class);
    }

    /*
     * @Override public UserDto updateUserNative(String userId, UserDto userDto)
     * throws RestApiException { String firstName = userDto.getFirstName(); String
     * lastName = userDto.getLastName(); userRepository.updateUserNative(userId,
     * firstName, lastName);
     * 
     * return modelMapper.map(userRepository.findByUserId(userId), UserDto.class); }
     */

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
	List<GrantedAuthority> authorities = new ArrayList<>();
	List<AuthorityEntity> authorityEntities = new ArrayList<>();

	Collection<RoleEntity> roles = userEntity.getRoles();
	if (roles == null) {
	    return authorities;
	}

	roles.forEach((role) -> {
	    authorities.add(new SimpleGrantedAuthority(role.getName()));
	    authorityEntities.addAll(role.getAuthorities());
	});

	authorityEntities.forEach((authorityEntity) -> {
	    authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
	});

	return authorities;
    }
}
