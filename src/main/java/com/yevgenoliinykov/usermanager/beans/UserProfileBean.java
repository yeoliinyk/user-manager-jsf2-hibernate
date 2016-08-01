package com.yevgenoliinykov.usermanager.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.yevgenoliinykov.usermanager.integration.UserDAO;
import com.yevgenoliinykov.usermanager.model.PhoneNumber;
import com.yevgenoliinykov.usermanager.model.Role;
import com.yevgenoliinykov.usermanager.model.User;

@Named
@ViewScoped
public class UserProfileBean implements Serializable {

    private static final long serialVersionUID = 3791377702952070044L;

    public static final String HOME_URL = "index.xhtml";

    @Inject
    private UserDAO userDAO;
    private User user;
    private String role;
    private PhoneNumber phoneNumber;

    private Set<Role> roles = new HashSet<>();
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		.getRequest();
	Long userId = Long.parseLong(request.getParameter("userId"));
	user = userDAO.findUserWithRolesById(userId);
	if (user == null) {
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    externalContext.redirect(HOME_URL);
	} else {
	    role = getUserRole(user);
	    if (!user.getPhoneNumbers().isEmpty()) {
		Iterator<PhoneNumber> phoneItr = user.getPhoneNumbers().iterator();
		phoneNumber = phoneItr.next();
	    } else {
		phoneNumber = new PhoneNumber(user);
	    }
	}
    }

    public String accept() {
	setUserRole(role, user);
	setUserPhoneNumber(phoneNumber, user);
	userDAO.updateUser(user);
	return "/search.xhtml?faces-redirect=true";
    }

    private String getUserRole(User user) {
	Iterator<Role> roleItr = user.getRoles().iterator();
	if (roleItr.next().getRole().equals(Role.Roles.USER)) {
	    return "user";
	} else {
	    return "admin";
	}
    }

    private void setUserRole(String role, User user) {
	Iterator<Role> itr = user.getRoles().iterator();
	Role userRole = itr.next();
	if (role.equals("user")) {
	    userRole.setRole(Role.Roles.USER);
	} else if (role.equals("admin")) {
	    userRole.setRole(Role.Roles.ADMIN);
	}
    }

    private void setUserPhoneNumber(PhoneNumber phoneNumber, User user) {
	phoneNumbers.add(phoneNumber);
	user.setPhoneNumbers(phoneNumbers);
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
