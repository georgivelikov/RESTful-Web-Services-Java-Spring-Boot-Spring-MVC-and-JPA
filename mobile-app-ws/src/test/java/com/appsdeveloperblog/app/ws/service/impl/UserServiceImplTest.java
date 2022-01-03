package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;

class UserServiceImplTest {

    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String email = "anymail@test.com";
    private final String rawPassword = "abcd1234";
    private final String publicId = "abcdefghijklmnop1234567";
    private final String encryptedPassword = "abcdefghij";
    private UserEntity userEntityStub;
    private UserDto userDtoStub;

    private AddressEntity addressStubShipping;
    private String addressTypeShipping = "shipping";
    private String addressCityShipping = "Sofia";
    private String addressCountryShipping = "Bulgaria";
    private String addressSteetShipping = "Test Street 1";
    private String addressPostalCodeShipping = "1000";

    private AddressEntity addressStubBilling;
    private String addressTypeBilling = "billing";
    private String addressCityBilling = "Varna";
    private String addressCountryBilling = "BG";
    private String addressSteetBilling = "Test Street 2";
    private String addressPostalCodeBilling = "1001";

    private AddressDto addressDtoShipping;
    private AddressDto addressDtoBilling;
    List<AddressDto> addressesDto;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    private Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptpasswordEncoder;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.openMocks(this);

	userEntityStub = new UserEntity();
	userEntityStub.setId(1L);
	userEntityStub.setFirstName(firstName);
	userEntityStub.setLastName(lastName);
	userEntityStub.setUserId(publicId);
	userEntityStub.setEncryptedPassword(encryptedPassword);
	userEntityStub.setEmail(email);

	List<AddressEntity> addresses = new ArrayList<AddressEntity>();

	addressStubShipping = new AddressEntity();
	addressStubShipping.setCity(addressCityShipping);
	addressStubShipping.setCountry(addressCountryShipping);
	addressStubShipping.setPostalCode(addressPostalCodeShipping);
	addressStubShipping.setStreet(addressSteetShipping);
	addressStubShipping.setType(addressTypeShipping);
	addresses.add(addressStubShipping);

	addressStubBilling = new AddressEntity();
	addressStubBilling.setCity(addressCityBilling);
	addressStubBilling.setCountry(addressCountryBilling);
	addressStubBilling.setPostalCode(addressPostalCodeBilling);
	addressStubBilling.setStreet(addressSteetBilling);
	addressStubBilling.setType(addressTypeBilling);
	addresses.add(addressStubBilling);

	userEntityStub.setAddresses(addresses);

	addressDtoShipping = new AddressDto();
	addressDtoShipping.setType(addressTypeShipping);
	addressDtoShipping.setCity(addressCityShipping);
	addressDtoShipping.setCountry(addressCountryShipping);
	addressDtoShipping.setPostalCode(addressPostalCodeShipping);
	addressDtoShipping.setStreet(addressSteetShipping);

	addressDtoBilling = new AddressDto();
	addressDtoBilling.setType(addressTypeBilling);
	addressDtoBilling.setCity(addressCityBilling);
	addressDtoBilling.setCountry(addressCountryBilling);
	addressDtoBilling.setPostalCode(addressPostalCodeBilling);
	addressDtoBilling.setStreet(addressSteetBilling);

	addressesDto = new ArrayList<>();
	addressesDto.add(addressDtoShipping);
	addressesDto.add(addressDtoBilling);

	userDtoStub = new UserDto();
	userDtoStub.setFirstName(firstName);
	userDtoStub.setLastName(lastName);
	userDtoStub.setPassword(rawPassword);
	userDtoStub.setEmail(email);
	userDtoStub.setAddresses(addressesDto);
    }

    @Test
    void testGetUser() throws RestApiException {
	when(userRepository.findByEmail(anyString())).thenReturn(userEntityStub);

	UserDto userDto = userService.getUser("anymail@test.com");

	assertNotNull(userDto);
	assertEquals(firstName, userDto.getFirstName());
	assertEquals(lastName, userDto.getLastName());
	assertEquals(publicId, userDto.getUserId());
	assertEquals(email, userDto.getEmail());
	assertEquals(encryptedPassword, userDto.getEncryptedPassword());
    }

    @Test
    void testGetUser_RestApiException() throws RestApiException {
	when(userRepository.findByEmail(anyString())).thenReturn(null);

	assertThrows(RestApiException.class, () -> {
	    userService.getUser(email);
	});
    }

    @Test
    void testCreateUser() throws RestApiException {
	when(userRepository.findByEmail(anyString())).thenReturn(null);
	when(utils.generatePublicId(anyInt())).thenReturn(publicId);
	when(bCryptpasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
	when(userRepository.save(any(UserEntity.class))).thenReturn(userEntityStub);

	UserDto storedUserDto = userService.createUser(userDtoStub);

	assertNotNull(storedUserDto);
	assertEquals(firstName, storedUserDto.getFirstName());
	assertEquals(lastName, storedUserDto.getLastName());
	assertEquals(publicId, storedUserDto.getUserId());
	assertEquals(encryptedPassword, storedUserDto.getEncryptedPassword());
	assertEquals(email, storedUserDto.getEmail());

	assertEquals(addressDtoShipping.getType(), storedUserDto.getAddresses().get(0).getType());
	assertEquals(addressDtoShipping.getCity(), storedUserDto.getAddresses().get(0).getCity());
	assertEquals(addressDtoShipping.getCountry(), storedUserDto.getAddresses().get(0).getCountry());
	assertEquals(addressDtoShipping.getStreet(), storedUserDto.getAddresses().get(0).getStreet());
	assertEquals(addressDtoShipping.getPostalCode(), storedUserDto.getAddresses().get(0).getPostalCode());

	assertEquals(addressDtoBilling.getType(), storedUserDto.getAddresses().get(1).getType());
	assertEquals(addressDtoBilling.getCity(), storedUserDto.getAddresses().get(1).getCity());
	assertEquals(addressDtoBilling.getCountry(), storedUserDto.getAddresses().get(1).getCountry());
	assertEquals(addressDtoBilling.getStreet(), storedUserDto.getAddresses().get(1).getStreet());
	assertEquals(addressDtoBilling.getPostalCode(), storedUserDto.getAddresses().get(1).getPostalCode());

	// 1 for the user public ID, and 2 times for the 2 addresses' public IDs = total
	// of 3 times for generatePublicId()
	verify(utils, times(3)).generatePublicId(anyInt());
	verify(bCryptpasswordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void testCreateUser_RestApiException() throws RestApiException {
	when(userRepository.findByEmail(anyString())).thenReturn(userEntityStub);

	assertThrows(RestApiException.class, () -> {
	    userService.createUser(userDtoStub);
	});
    }
}
