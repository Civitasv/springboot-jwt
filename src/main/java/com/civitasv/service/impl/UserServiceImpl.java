package com.civitasv.service.impl;

import com.civitasv.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.civitasv.mapper.UserMapper;
import com.civitasv.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserMapper mapper;

    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User get(String userId, String password) {
        return mapper.get(userId, password);
    }

    @Override
    public User getByUserName(String username) {
        return mapper.getByUserName(username);
    }

    @Override
    public int add(User user) {
        return mapper.add(user);
    }

    @Override
    public int delete(String id) {
        return mapper.delete(id);
    }

    @Override
    public int update(User user) {
        return mapper.update(user);
    }

    @Override
    public String getPwdByUserName(String username) {
        return mapper.getPwdByUserName(username);
    }

    @Override
    public List<User> getAll() {
        return mapper.getAll();
    }

    @Override
    public List<User> getUsersByUserName(String username) {
        return mapper.getUsersByUserName(username);
    }
}
