package com.yevgenoliinykov.usermanager.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.jboss.logging.Logger;

import com.yevgenoliinykov.usermanager.integration.UserDAO;
import com.yevgenoliinykov.usermanager.model.User;

@Named
@ViewScoped
public class SearchBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SearchBean.class);

    private static final long serialVersionUID = -2215309108086038638L;
    public static final String HOME_URL = "index.xhtml";

    @Inject
    private UserDAO userDAO;
    private List<User> users;

    private String loginToSearch = "";
    private String telToSearch = "";
    private String statusString = "";
    private boolean renderSearchResult;

    public void search() {
	if (loginToSearch.length() > 2 || telToSearch.length() > 2) {
	    LOGGER.info("Login to search is " + loginToSearch + " and Tel to search is " + telToSearch);
	    users = userDAO.findUsersByLoginOrTel(loginToSearch, telToSearch);
	    if (!users.isEmpty()) {
		renderSearchResult = true;
	    } else {
		statusString = "No users with this parametrs";
		renderSearchResult = false;
	    }
	} else {
	    statusString = "Please, enter at least 3 characters";
	    renderSearchResult = false;
	}
    }

    public void editUserProfile(Long id) throws IOException {
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	externalContext.redirect("userProfile.xhtml?userId=" + id);
    }

    public void deleteUser(Long id) {
	users.remove(findUserByIdInList(users, id));
	userDAO.deleteUser(id);
    }

    public void logout() throws IOException {
	SecurityUtils.getSubject().logout();
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	externalContext.invalidateSession();
	externalContext.redirect(HOME_URL);
    }

    private User findUserByIdInList(List<User> users, Long id) {
	User targetUser = null;
	for (User user : users) {
	    if (user.getUserId() == id) {
		targetUser = user;
	    }
	}
	return targetUser;
    }

    public String getLoginToSearch() {
	return loginToSearch;
    }

    public void setLoginToSearch(String loginToSearch) {
	this.loginToSearch = loginToSearch;
    }

    public String getTelToSearch() {
	return telToSearch;
    }

    public void setTelToSearch(String telToSearch) {
	this.telToSearch = telToSearch;
    }

    public List<User> getUsers() {
	return users;
    }

    public void setUsers(List<User> users) {
	this.users = users;
    }

    public String getStatusString() {
	return statusString;
    }

    public void setStatusString(String statusString) {
	this.statusString = statusString;
    }

    public boolean isRenderSearchResult() {
	return renderSearchResult;
    }

    public void setRenderSearchResult(boolean renderSearchResult) {
	this.renderSearchResult = renderSearchResult;
    }
}