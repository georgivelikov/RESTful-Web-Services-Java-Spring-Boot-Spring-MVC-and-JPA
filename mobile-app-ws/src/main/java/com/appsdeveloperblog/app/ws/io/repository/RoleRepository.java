package com.appsdeveloperblog.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    public RoleEntity findByName(String name);
}
