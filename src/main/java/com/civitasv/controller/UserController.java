package com.civitasv.controller;

import com.civitasv.authority.VerifyToken;
import com.civitasv.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.civitasv.handler.Result;
import com.civitasv.handler.ResultCode;
import com.civitasv.service.TokenService;
import com.civitasv.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public String login(@RequestBody User requestUser, HttpServletResponse response) {
        String username = requestUser.getUsername();
        String password = requestUser.getPassword();
        User user = userService.get(username, password);
        if (!Objects.isNull(user)) { // 验证成功，可登录
            Map<String, Object> accessTokenInfo = tokenService.getJWTToken(user); // 获得access token
            Map<String, Object> refreshTokenInfo = tokenService.getRefreshToken(user.getUsername()); // 获得refresh token
            Map<String, Object> map = new HashMap<>();
            map.put("jwt_token", accessTokenInfo.get("jwtToken"));
            map.put("jwt_token_expiry", accessTokenInfo.get("jwtTokenExpiry"));

            // 将 refresh token 加入httponly cookie
            Cookie cookie = new Cookie("refresh_token", refreshTokenInfo.get("refreshToken").toString());
            cookie.setMaxAge(Integer.parseInt(refreshTokenInfo.get("refreshTokenMaxAge").toString()));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return new Result<Map<String, Object>>().success(true).message("登录成功").code(ResultCode.OK).data(map).toString();
        } else {
            return new Result<String>().success(false).message("登陆失败, 请检查用户名和密码").code(ResultCode.NOT_FOUND).toString();
        }
    }

    @GetMapping("logout")
    public String logout(@CookieValue(value = "refresh_token", defaultValue = "") String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        if (refreshToken.isEmpty()) {
            return new Result<Map<String, Object>>().success(true).message("退出成功").code(ResultCode.OK).toString();
        }
        if (tokenService.isExpire(refreshToken)) {
            return new Result<Map<String, Object>>().success(true).message("退出成功").code(ResultCode.OK).toString();
        }
        // 清除token
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                .filter(cookie1 -> "refresh_token".equals(cookie1.getName()))
                .findFirst();
        if (cookieOptional.isPresent()) {
            Cookie cookie = cookieOptional.get();
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return new Result<Map<String, Object>>().success(true).message("退出成功").code(ResultCode.OK).toString();
    }
}
