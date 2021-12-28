package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    // Syntax 'findBy{fieldName}' doesn't need implementation
    public UserEntity findByEmail(String email);

    public UserEntity findByUserId(String id);
}
