package com.example.travel_planner.controller;

import com.example.travel_planner.config.JwtTokenProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.service.CommentService;
import com.example.travel_planner.service.LikeService;
import com.example.travel_planner.service.PlanService;
import com.example.travel_planner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity kakaoLogin(@RequestParam String token) {
        return userService.getUserInfoKakao(token);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> data) {
        return userService.login(data);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity checkEmail(@RequestBody Map<String, String> email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return userService.getUserInfo(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Users user) {
        return userService.register(user);
    }

    @PostMapping("/tokenAuth") // 그저 테스트
    public ResponseEntity tokenAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if(jwtTokenProvider.validateAccessToken(token.split(" ")[1])){
            return new StatusCode(HttpStatus.OK, "인증 성공").sendResponse();
        }else{
            return new StatusCode(HttpStatus.UNAUTHORIZED, "만료된 토큰").sendResponse();
        }
    }

    @PostMapping("/getTokenUsedRefreshToken")
    public ResponseEntity getTokenUsedRefreshToken(@RequestBody Map<String, String> data){
        return userService.getTokenUsedRefreshToken(data);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(MultipartFile[] multipartFiles){
        return userService.uploadFile(multipartFiles);
    }
}
