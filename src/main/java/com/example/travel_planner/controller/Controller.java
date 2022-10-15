package com.example.travel_planner.controller;

import com.example.travel_planner.service.CommentService;
import com.example.travel_planner.service.LikeService;
import com.example.travel_planner.service.PlanService;
import com.example.travel_planner.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
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

    @GetMapping("/kakaoCallback")
    public Map<String, Object> kakaoLogin(@RequestParam String code){
        return userService.getUserInfoKakao(code);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> data){
        return userService.login(data);
    }
}

