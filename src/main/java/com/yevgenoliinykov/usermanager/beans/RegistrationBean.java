package com.yevgenoliinykov.usermanager.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.yevgenoliinykov.usermanager.integration.UserDAO;
import com.yevgenoliinykov.usermanager.model.PhoneNumber;
import com.yevgenoliinykov.usermanager.model.Role;
import com.yevgenoliinykov.usermanager.model.User;

@Named
@RequestScoped
public class RegistrationBean {

    @Inject
    private UserDAO userDAO;
    @Inject
    private User user;
    @Inject
    private PhoneNumber phoneNumber;
    private String role;

    List<PhoneNumber> phoneNumbers = new ArrayList<>();
    Set<Role> roles = new HashSet<>();

    public String createUser() {
	if (userDAO.isLoginInDB(user.getLogin())) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    context.addMessage(null, new FacesMessage("User with such login already exists!"));
	    RequestContext.getCurrentInstance().update("msg");
	    return "emailInDb";
	} else {
	    setUserRole(role, user);
	    setUserPhoneNumber(phoneNumber, user);
	    userDAO.createUser(user);
	    return "/index.xhtml?faces-redirect=true";
	}
    }

    private void setUserPhoneNumber(PhoneNumber phoneNumber, User user) {
	phoneNumber.setUser(user);
	phoneNumbers.add(phoneNumber);
	user.setPhoneNumbers(phoneNumbers);
    }

    private void setUserRole(String role, User user) {
	Role userRole = new Role(user);
	if (role.equals("user")) {
	    userRole.setRole(Role.Roles.USER);
	} else if (role.equals("admin")) {
	    userRole.setRole(Role.Roles.ADMIN);
	}
	roles.add(userRole);
	user.setRoles(roles);
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public PhoneNumber getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

}
