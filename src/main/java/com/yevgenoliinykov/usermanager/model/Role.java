package com.yevgenoliinykov.usermanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLES")
public class Role implements Serializable {

    private static final long serialVersionUID = -9154488537227145615L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id", unique = true, nullable = false)
    private Long userRoleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Role() {
	super();
    }

    public Role(User user) {
	super();
	this.user = user;
    }

    public Long getUserRoleId() {
	return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
	this.userRoleId = userRoleId;
    }

    public Roles getRole() {
	return role;
    }

    public void setRole(Roles role) {
	this.role = role;
    }

    public enum Roles {
	USER, ADMIN;
    }
}
