package com.example.travel_planner.controller;

import com.example.travel_planner.service.CommentService;
import com.example.travel_planner.service.LikeService;
import com.example.travel_planner.service.PlanService;
import com.example.travel_planner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    private UserService userService;
    @Autowired
    private PlanService planService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/kakaoLogin")
    public ResponseEntity kakaoLogin(@RequestParam String token){
        return userService.getUserInfoKakao(token);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> data){
        return userService.login(data);
    }

    @GetMapping("/sign")
    public String signUp(){
        return "/sign";
    }

    @GetMapping("/checkEmail")
    public ResponseEntity checkEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.checkEmail(token);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.getUserInfo();
    }
}

