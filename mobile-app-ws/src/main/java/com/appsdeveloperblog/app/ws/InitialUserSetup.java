package com.appsdeveloperblog.app.ws;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.appsdeveloperblog.app.ws.io.entity.AuthorityEntity;
import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.AuthorityRepository;
import com.appsdeveloperblog.app.ws.io.repository.RoleRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.utils.Utils;

@Component
public class InitialUserSetup {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Utils utils;

    private boolean adminCreated = true;

    @EventListener @Transactional
    public void onApplicationEvent(ApplicationReadyEvent ev) {

	AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
	AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
	AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

	RoleEntity roleUser = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
	RoleEntity roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

	if (adminCreated) {
	    return;
	}

	createAdmin(Arrays.asList(roleAdmin));
    }

    @Transactional
    private AuthorityEntity createAuthority(String name) {
	AuthorityEntity authority = authorityRepository.findByName(name);
	if (authority == null) {
	    authority = new AuthorityEntity(name);
	    authorityRepository.save(authority);
	}

	return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
	RoleEntity role = roleRepository.findByName(name);
	if (role == null) {
	    if (name.equals("ROLE_ADMIN")) {
		adminCreated = false;
	    }

	    role = new RoleEntity(name);
	    role.setAuthorities(authorities);
	    roleRepository.save(role);
	}

	return role;
    }

    @Transactional
    private UserEntity createAdmin(Collection<RoleEntity> roles) {
	UserEntity admin = new UserEntity();
	admin.setFirstName("Administrator");
	admin.setLastName("Administrator");
	admin.setEmail("admin@test.com");
	admin.setUserId(utils.generatePublicId(30));
	admin.setEncryptedPassword(bCryptPasswordEncoder.encode("abcd1234"));
	admin.setRoles(roles);

	return userRepository.save(admin);
    }
}
