package com.civitasv.controller;

import com.civitasv.authority.VerifyToken;
import com.civitasv.handler.Result;
import com.civitasv.handler.ResultCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("")
public class PageController {
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @VerifyToken(url = "/manage")
    @GetMapping("/manage")
    public String manage() {
        return new Result<String>().success(true).message("进入管理页面").code(ResultCode.OK).data("进入管理页面").toString();
    }

    @VerifyToken(url = "/normal")
    @GetMapping("/normal")
    public String normal() {
        return new Result<String>().success(true).message("进入用户页面").code(ResultCode.OK).data("进入用户页面").toString();
    }
}
