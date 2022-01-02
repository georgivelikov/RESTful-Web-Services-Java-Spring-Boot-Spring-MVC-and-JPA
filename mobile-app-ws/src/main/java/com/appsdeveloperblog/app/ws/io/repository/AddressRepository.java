package com.appsdeveloperblog.app.ws.io.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    public AddressEntity findByAddressId(String addressId);

    public List<AddressEntity> findAllByUserDetails(UserEntity userDetails);
}
