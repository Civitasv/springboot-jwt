package com.civitasv.mapper;

import com.civitasv.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User get(String id, String password);

    User getByUserId(String id);

    int add(User user);

    int delete(String id);

    int update(User user);

    String getPwdByUserId(String id);

    List<User> getAll();

    List<User> getUsersByName(String name);

    List<User> getUsersByPhone(String phone);
}
