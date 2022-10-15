package com.example.travel_planner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/hello")
    public String hello(){
        return "스프링부트 리액트 프록시로 연결중";
    }
}
