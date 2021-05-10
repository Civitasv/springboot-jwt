package com.civitasv.mapper;

import com.civitasv.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User get(String username, String password);

    User getByUserName(String username);

    int add(User user);

    int delete(String id);

    int update(User user);

    String getPwdByUserName(String username);

    List<User> getAll();

    List<User> getUsersByUserName(String username);
}
