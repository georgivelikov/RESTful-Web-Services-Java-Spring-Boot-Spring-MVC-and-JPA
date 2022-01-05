package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
// PagingAndSortingRepository extends CrudRepository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    // Syntax 'findBy{fieldName}' doesn't need implementation
    public UserEntity findByEmail(String email);

    public UserEntity findByUserId(String id);

    /*
     * @Transactional
     * 
     * @Modifying
     * 
     * @Query(value =
     * "update user u set u.FIRST_NAME=:firstName, u.LAST_NAME=:lastName where u.user_id=:userId"
     * , nativeQuery = true) void updateUserNative(@Param(value = "userId") String
     * userId, @Param(value = "firstName") String firstName,
     * 
     * @Param(value = "lastName") String lastName);
     */

    @Transactional
    @Modifying
    @Query(value = "update UserEntity u set u.firstName=:firstName, u.lastName=:lastName where u.userId=:userId")
    public int updateUserJpql(String userId, String firstName, String lastName);
}
