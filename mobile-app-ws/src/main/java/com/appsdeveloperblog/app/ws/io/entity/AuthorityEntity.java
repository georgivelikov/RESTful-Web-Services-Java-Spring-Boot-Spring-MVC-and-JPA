package com.appsdeveloperblog.app.ws.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class AuthorityEntity implements Serializable {

    private static final long serialVersionUID = 4638664597353731031L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles;

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

    public Collection<RoleEntity> getRoles() {
	return roles;
    }

    public void setRoles(Collection<RoleEntity> roles) {
	this.roles = roles;
    }
}
