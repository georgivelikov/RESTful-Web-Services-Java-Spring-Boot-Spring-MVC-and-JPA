package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import com.appsdeveloperblog.app.ws.exception.RestApiException;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

public interface AddressService {

    public AddressDto getAddressById(String addressId) throws RestApiException;

    public List<AddressDto> getAddresses(String userId) throws RestApiException;
}
