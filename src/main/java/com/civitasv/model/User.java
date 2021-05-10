package com.civitasv.model;

import java.util.List;

import lombok.Data;

@Data
public class User {
    // 名字
    private String username;
    // 密码
    private String password;
    // 角色
    private List<Role> roles;
}
