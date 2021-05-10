package com.civitasv.service;

import com.civitasv.model.User;

import java.util.List;

public interface UserService {
    User get(String username, String password);

    User getByUserName(String username);

    int add(User user);

    int delete(String id);

    int update(User user);

    String getPwdByUserName(String username);

    List<User> getAll();

    List<User> getUsersByUserName(String name);
}
