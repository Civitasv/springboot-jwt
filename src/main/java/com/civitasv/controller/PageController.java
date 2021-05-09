package com.civitasv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.civitasv.authority.VerifyToken;

@Controller
@RequestMapping("")
public class PageController {
    @GetMapping("/")
    public String login() {
        return "login";
    }
}
