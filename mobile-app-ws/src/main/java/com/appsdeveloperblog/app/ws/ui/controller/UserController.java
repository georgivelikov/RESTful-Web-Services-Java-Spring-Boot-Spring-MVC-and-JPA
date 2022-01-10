package com.appsdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exception.ExceptionMessages;
import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressRest;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.ui.model.response.operations.OperationName;
import com.appsdeveloperblog.app.ws.ui.model.response.operations.OperationResult;
import com.appsdeveloperblog.app.ws.ui.model.response.operations.OperationStatusRest;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    // using ModelMapper from www.modelmapper.org
    private ModelMapper modelMapper = new ModelMapper();

    // 'consumes' and 'produces' may be needed for good practice
    @GetMapping(path = "/{userId}")
    public UserRest getUser(@PathVariable String userId) throws RestApiException {
	UserDto userDto = userService.getUserByUserId(userId);
	return modelMapper.map(userDto, UserRest.class);
    }

    @GetMapping(path = "/welcome")
    public String welcome() throws RestApiException {
	return "Welcome!";
    }

    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    @GetMapping(path = "/protected")
    public String protectedResource() throws RestApiException {
	return "Welcome to protected resourse!";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws RestApiException {
	UserRest returnValue = new UserRest();

	// Added to test Exception handling
	if (Utils.isNullOrBlank(userDetails.getFirstName())) {
	    throw new RestApiException(ExceptionMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
	}

	UserDto userDto = modelMapper.map(userDetails, UserDto.class);

	UserDto createdUser = userService.createUser(userDto);
	returnValue = modelMapper.map(createdUser, UserRest.class);

	return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	    throws RestApiException {
	UserDto userDto = modelMapper.map(userDetails, UserDto.class);

	UserDto updatedUser = userService.updateUser(id, userDto);

	return modelMapper.map(updatedUser, UserRest.class);
    }

    @PutMapping(path = "/{id}/jpql")
    public UserRest updateUserJpql(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	    throws RestApiException {
	UserDto userDto = modelMapper.map(userDetails, UserDto.class);

	UserDto updatedUser = userService.updateUserJpql(id, userDto);

	return modelMapper.map(updatedUser, UserRest.class);
    }

    /*
     * @PutMapping(path = "/{id}/native") public UserRest
     * updateUserNative(@PathVariable String id, @RequestBody
     * UserDetailsRequestModel userDetails) throws RestApiException { UserDto
     * userDto = modelMapper.map(userDetails, UserDto.class);
     * 
     * UserDto updatedUser = userService.updateUserNative(id, userDto);
     * 
     * return modelMapper.map(updatedUser, UserRest.class); }
     */

    @PostAuthorize("hasRole('ADMIN')")
    // @Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/{id}")
    public OperationStatusRest deleteUser(@PathVariable String id) throws RestApiException {
	OperationStatusRest returnValue = new OperationStatusRest();

	userService.deleteUser(id);

	returnValue.setOperationName(OperationName.DELETE.name());
	returnValue.setOperationResult(OperationResult.SUCCESS.name());

	return returnValue;
    }

    @GetMapping()
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
	    @RequestParam(value = "limit", defaultValue = "20") int limit) throws RestApiException {
	List<UserRest> returnValue = new ArrayList<>();

	// In the Repository implementation pagination starts with '0', but in UI
	// usually pages start from 1, 2, 3 etc. So UI will send the number of the page,
	// which should be reduced by 1
	if (page > 0) {
	    page -= 1;
	}

	List<UserDto> users = userService.getUsers(page, limit);

	for (UserDto userDto : users) {
	    UserRest userRest = modelMapper.map(userDto, UserRest.class);
	    returnValue.add(userRest);
	}

	return returnValue;
    }

    @GetMapping(path = "/{userId}/addresses")
    public List<AddressRest> getUserAddresses(@PathVariable String userId) throws RestApiException {
	List<AddressRest> returnValue = new ArrayList<>();

	// Addresses can be taken using userService.findUserById(userId).getAddesses().
	// The bellow implementation just shows how it could be done with AddessService
	// and AddessRepository for practice
	List<AddressDto> addressesDto = addressService.getAddresses(userId);
	if (addressesDto != null && !addressesDto.isEmpty()) {
	    Type listType = new TypeToken<List<AddressRest>>() {
	    }.getType();

	    returnValue = modelMapper.map(addressesDto, listType);

	    for (AddressRest addressRest : returnValue) {
		Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
			.slash(userId)
			.slash("addresses")
			.slash(addressRest.getAddressId())
			.withSelfRel();
		addressRest.add(selfLink);
	    }
	}

	return returnValue;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}")
    public AddressRest getUserAddress(@PathVariable String userId, @PathVariable String addressId)
	    throws RestApiException {
	AddressDto addressDto = addressService.getAddressById(addressId);
	AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);

	// HATEOAS - adding links to the response. Could be done with
	// EntityModel<AddressRest> as well. 'methodOn' pattern can be used
	// -->
	// Link building for http://localhost:port//users/{userId}
	Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
	returnValue.add(userLink);

	// Link building for http://localhost:port//users/{userId}/addresses
	Link userAddressesLink = WebMvcLinkBuilder.linkTo(UserController.class)
		.slash(userId)
		.slash("addresses")
		.withRel("addresses");
	returnValue.add(userAddressesLink);

	// Link building for http://localhost:port//users/{userId}/addresses/{id}
	Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
		.slash(userId)
		.slash("addresses")
		.slash(addressId)
		.withSelfRel();
	returnValue.add(selfLink);

	return returnValue;
    }
}
