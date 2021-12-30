package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.ui.model.response.operations.OperationName;
import com.appsdeveloperblog.app.ws.ui.model.response.operations.OperationResult;
import com.appsdeveloperblog.app.ws.ui.model.response.operations.OperationStatusRest;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    // 'consumes' and 'produces' may be needed for good practice
    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id) throws RestApiException {
	UserRest returnValue = new UserRest();
	UserDto userDto = userService.getUserById(id);
	BeanUtils.copyProperties(userDto, returnValue);

	return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws RestApiException {
	UserRest returnValue = new UserRest();

	// Added to test Exception handling
	if (Utils.isNullOrBlank(userDetails.getFirstName())) {
	    throw new RestApiException(ExceptionMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
	}

	UserDto userDto = new UserDto();
	BeanUtils.copyProperties(userDetails, userDto);

	UserDto createdUser = userService.createUser(userDto);
	BeanUtils.copyProperties(createdUser, returnValue);

	return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	    throws RestApiException {
	UserRest returnValue = new UserRest();
	UserDto userDto = new UserDto();
	BeanUtils.copyProperties(userDetails, userDto);

	UserDto updatedUser = userService.updateUser(id, userDto);
	BeanUtils.copyProperties(updatedUser, returnValue);

	return returnValue;
    }

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
	    UserRest userRest = new UserRest();
	    BeanUtils.copyProperties(userDto, userRest);
	    returnValue.add(userRest);
	}

	return returnValue;
    }
}
