package com.yevgenoliinykov.usermanager.beans;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.primefaces.context.RequestContext;

import com.yevgenoliinykov.usermanager.model.Role.Roles;

@Named
@RequestScoped
public class LogInBean {

    public static final String SEARCH_URL = "search.xhtml";

    private String login;
    private String password;
    private boolean rememberMe;
    Subject subject;

    @PostConstruct
    public void init() throws IOException {
	subject = SecurityUtils.getSubject();
	if (subject.getPrincipal() != null) {
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    externalContext.redirect(SEARCH_URL);
	}
    }

    public void submit() throws IOException {
	FacesContext context = FacesContext.getCurrentInstance();
	try {
	    subject.login(new UsernamePasswordToken(login, password, rememberMe));
	    if (subject.hasRole(Roles.ADMIN.toString())) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(SEARCH_URL);
	    } else {
		subject.logout();
		context.addMessage(null, new FacesMessage("Only admins may enter"));
		RequestContext.getCurrentInstance().update("msg");
	    }

	} catch (AuthenticationException e) {
	    context.addMessage(null, new FacesMessage("Wrong password or login!"));
	    RequestContext.getCurrentInstance().update("msg");
	}
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public boolean isRememberMe() {
	return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
	this.rememberMe = rememberMe;
    }
}
