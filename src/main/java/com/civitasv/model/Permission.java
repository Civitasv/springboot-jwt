package com.civitasv.model;

import lombok.Data;

import java.util.List;

@Data
public class Permission {
    private Integer id;
    private String name; // 名字
    private String description; // 详细介绍权限
    private String url; // url
    private List<Role> roles; // 具有该权限的所有角色
}
