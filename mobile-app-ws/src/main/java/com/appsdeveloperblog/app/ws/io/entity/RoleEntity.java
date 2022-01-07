package com.appsdeveloperblog.app.ws.io.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 5605260522147928803L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    @ManyToMany(cascade = { CascadeType.PERSIST
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "roles_authorities", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
	    inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Collection<RoleEntity> authorities;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Collection<UserEntity> getUsers() {
	return users;
    }

    public void setUsers(List<UserEntity> users) {
	this.users = users;
    }

    public Collection<RoleEntity> getAuthorities() {
	return authorities;
    }

    public void setAuthorities(Collection<RoleEntity> authorities) {
	this.authorities = authorities;
    }

}
