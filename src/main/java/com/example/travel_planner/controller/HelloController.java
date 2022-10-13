package com.example.travel_planner.controller;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

public class HelloController {
    @GetMapping("/api/hello")
    public List<String> Hello(){
        return Arrays.asList("서버서버", "뷰뷰");
    }
}
