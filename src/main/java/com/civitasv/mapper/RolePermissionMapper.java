package com.civitasv.mapper;

import com.civitasv.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionMapper {
    int add(RolePermission rolePermission);
}
