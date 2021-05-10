package com.civitasv.service;


import com.civitasv.model.User;

import java.util.List;
import java.util.Map;

public interface TokenService {
    Map<String, Object> getJWTToken(User user);

    Map<String, Object> getRefreshToken(String username);

    String getUsernameFromToken(String accessToken);

    List<String> getPermissions(String accessToken);

    boolean isExpire(String accessToken);
}
