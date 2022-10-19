package com.example.travel_planner.controller;

import com.example.travel_planner.config.JwtTokenProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.service.CommentService;
import com.example.travel_planner.service.LikeService;
import com.example.travel_planner.service.PlanService;
import com.example.travel_planner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
    public ResponseEntity uploadFile(MultipartFile[] multipartFiles, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.uploadFile(multipartFiles, token);
    }

    @GetMapping(value="/image/view", produces= MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("value") String value) throws IOException{
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String fileDir = "C:\\Users\\YJ\\Downloads\\spring boot\\travel_planner\\build\\classes\\java\\main\\resources\\images\\" + value; // 파일경로

        try{
            fis = new FileInputStream(fileDir);
        } catch(Exception e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }
}
