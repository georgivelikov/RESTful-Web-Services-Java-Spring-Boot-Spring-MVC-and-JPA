package com.appsdeveloperblog.app.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

class UserControllerTest {

    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String email = "anymail@test.com";
    private final String rawPassword = "abcd1234";
    private final String userId = "abcdefghijklmnop1234567";
    private final String encryptedPassword = "abcdefghij";
    private UserDto userDtoStub;

    private String addressTypeShipping = "shipping";
    private String addressCityShipping = "Sofia";
    private String addressCountryShipping = "Bulgaria";
    private String addressSteetShipping = "Test Street 1";
    private String addressPostalCodeShipping = "1000";

    private String addressTypeBilling = "billing";
    private String addressCityBilling = "Varna";
    private String addressCountryBilling = "BG";
    private String addressSteetBilling = "Test Street 2";
    private String addressPostalCodeBilling = "1001";

    private AddressDto addressDtoShipping;
    private AddressDto addressDtoBilling;
    List<AddressDto> addressesDto;

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.openMocks(this);

	userDtoStub = new UserDto();
	userDtoStub.setFirstName(firstName);
	userDtoStub.setLastName(lastName);
	userDtoStub.setPassword(rawPassword);
	userDtoStub.setEmail(email);
	userDtoStub.setUserId(userId);
	userDtoStub.setEncryptedPassword(encryptedPassword);

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

	userDtoStub.setAddresses(addressesDto);

    }

    @Test
    void testGetUser() throws RestApiException {
	when(userService.getUserByUserId(anyString())).thenReturn(userDtoStub);

	UserRest userRest = userController.getUser(userId);

	assertNotNull(userRest);
	assertEquals(userDtoStub.getUserId(), userRest.getUserId());
	assertEquals(userDtoStub.getFirstName(), userRest.getFirstName());
	assertEquals(userDtoStub.getLastName(), userRest.getLastName());
	assertEquals(userDtoStub.getEmail(), userRest.getEmail());
	assertTrue(userDtoStub.getAddresses().size() == userRest.getAddresses().size());
    }

}
