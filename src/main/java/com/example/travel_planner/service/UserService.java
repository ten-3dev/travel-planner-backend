package com.example.travel_planner.service;

import com.example.travel_planner.config.KakaoProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.UserRepository;
import com.example.travel_planner.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity getUserInfoKakao(String token){
        KakaoProvider kakaoProvider = new KakaoProvider();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        Map userInfo = kakaoProvider.getUserInfo(token);
        if(userInfo != null){
            Optional<Users> resultEmail = userRepository.findById((String) userInfo.get("email"));
            if(resultEmail.isPresent()){
                Map<String, String> tokens = jwtTokenProvider.generateToken(resultEmail.get().getEmail());
                tokens.put("isUser", "Y");
                return new StatusCode(HttpStatus.OK, tokens, "로그인 성공!").sendResponse();
            }
            userInfo.put("isUser", "N");
            return new StatusCode(HttpStatus.OK, userInfo, "유저 정보 조회 성공").sendResponse();
        }
        return new StatusCode(HttpStatus.BAD_REQUEST, "알 수 없는 오류로 잠시 후 로그인을 시도해주세요.").sendResponse();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity login(Map<String, String> data){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Optional<Users> resultEmail = userRepository.findById(data.get("email"));
        if(resultEmail.isPresent()){
            if(!resultEmail.get().getPassword().equals(data.get("pw"))) {
                return new StatusCode(HttpStatus.NOT_FOUND, "로그인 실패! 비밀번호를 확인해주세요.").sendResponse();
            }
            Map<String, String> tokens = jwtTokenProvider.generateToken(resultEmail.get().getEmail());
            return new StatusCode(HttpStatus.OK, tokens, "로그인 성공!").sendResponse();
        }
        return new StatusCode(HttpStatus.NOT_FOUND, "로그인 실패! 로그인 또는 비밀번호를 확인해주세요.").sendResponse();
    }
}
