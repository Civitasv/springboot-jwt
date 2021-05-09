package com.civitasv.mapper;

import com.civitasv.model.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int add(UserRole userRole);
}
