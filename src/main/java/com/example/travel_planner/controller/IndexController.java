package com.example.travel_planner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    @GetMapping("/signForm")
    public String signForm() {
        return "회원가입해";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "로그인해라";
    }
}
