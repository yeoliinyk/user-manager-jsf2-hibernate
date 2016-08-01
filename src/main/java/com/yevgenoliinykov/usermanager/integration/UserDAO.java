package com.yevgenoliinykov.usermanager.integration;

import java.util.List;

import com.yevgenoliinykov.usermanager.model.User;

public interface UserDAO {
    public void createUser(User user);

    public void updateUser(User user);

    public void deleteUser(Long id);

    public boolean isLoginInDB(String login);

    public User findUserWithRolesById(Long id);

    public List<User> findUsersByLoginOrTel(String login, String tel);
}
