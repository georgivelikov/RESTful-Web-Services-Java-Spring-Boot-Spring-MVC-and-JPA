package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.app.ws.io.entity.AuthorityEntity;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    public AuthorityEntity findByName(String name);
}
