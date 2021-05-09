package com.civitasv.controller;

import com.civitasv.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.civitasv.handler.Result;
import com.civitasv.handler.ResultCode;
import com.civitasv.service.TokenService;
import com.civitasv.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("token")
public class TokenController {
    private TokenService tokenService;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("refresh")
    public String refresh(@CookieValue(value = "refresh_token", defaultValue = "") String refreshToken, HttpServletResponse response) {
        // 验证 refresh token
        if (refreshToken.isEmpty()) {
            return new Result<Map<String, Object>>().success(true).message("token不可以为空").code(ResultCode.AUTH_NEED).toString();
        }
        if (tokenService.isExpire(refreshToken)) {
            return new Result<Map<String, Object>>().success(true).message("刷新token已经失效").code(ResultCode.AUTH_NEED).toString();
        }
        // 获取userId
        String userId = tokenService.getUserIdFromToken(refreshToken);
        if (Objects.isNull(userId)) {
            return new Result<Map<String, Object>>().success(true).message("刷新token已经失效").code(ResultCode.AUTH_NEED).toString();
        }
        // 根据userId获取user
        User user = userService.getByUserId(userId);
        // 重新生成 access token 和 refresh token
        Map<String, Object> accessTokenInfo = tokenService.getAccessToken(user); // 获得access token
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessTokenInfo.get("accessToken"));
        map.put("access_token_expiry", accessTokenInfo.get("accessTokenExpiry"));
        map.put("user_id", user.getId());
        map.put("user_name", user.getName());

        Map<String, Object> refreshTokenInfo = tokenService.getRefreshToken(userId);
        // 将 refresh token 加入httponly cookie
        Cookie cookie = new Cookie("refresh_token", refreshTokenInfo.get("refreshToken").toString());
        cookie.setMaxAge(Integer.parseInt(refreshTokenInfo.get("refreshTokenMaxAge").toString()));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new Result<Map<String, Object>>().success(true).message("刷新成功").code(ResultCode.OK).data(map).toString();
    }
}
