package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
// PagingAndSortingRepository extends CrudRepository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    // Syntax 'findBy{fieldName}' doesn't need implementation
    public UserEntity findByEmail(String email);

    public UserEntity findByUserId(String id);
}
