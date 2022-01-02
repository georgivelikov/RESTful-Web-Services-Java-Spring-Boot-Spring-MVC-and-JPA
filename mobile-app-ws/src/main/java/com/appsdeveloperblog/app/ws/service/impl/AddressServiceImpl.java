package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.ExceptionMessages;
import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {
	List<AddressDto> returnValue = new ArrayList<>();

	UserEntity userEntity = userRepository.findByUserId(userId);
	if (userEntity == null) {
	    return returnValue;
	}

	List<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
	for (int i = 0; i < addresses.size(); i++) {
	    returnValue.add(modelMapper.map(addresses.get(i), AddressDto.class));
	}

	return returnValue;
    }

    @Override
    public AddressDto getAddressById(String addressId) throws RestApiException {
	AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
	if (addressEntity == null) {
	    throw new RestApiException(ExceptionMessages.NO_RECORD_FOUND.getErrorMessage());
	}

	return modelMapper.map(addressEntity, AddressDto.class);
    }

}
