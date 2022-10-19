package com.example.travel_planner.service;

import com.example.travel_planner.config.KakaoProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.UserRepository;
import com.example.travel_planner.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @SuppressWarnings("uncheked")
    public ResponseEntity getUserInfoKakao(String token) {
        KakaoProvider kakaoProvider = new KakaoProvider();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        Map userInfo = kakaoProvider.getUserInfo(token);
        if (userInfo != null) {
            Optional<Users> resultEmail = userRepository.findById((String) userInfo.get("email"));
            if (resultEmail.isPresent()) {
                Map<String, String> tokens = jwtTokenProvider.generateToken(resultEmail.get().getEmail());
                tokens.put("isUser", "Y");
                return new StatusCode(HttpStatus.OK, tokens, "로그인 성공!").sendResponse();
            }
            userInfo.put("isUser", "N");
            return new StatusCode(HttpStatus.OK, userInfo, "유저 정보 조회 성공").sendResponse();
        }
        return new StatusCode(HttpStatus.UNAUTHORIZED, "알 수 없는 오류로 잠시 후 로그인을 시도해주세요.").sendResponse();
    }

    public ResponseEntity login(Map<String, String> data) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Optional<Users> resultEmail = userRepository.findById(data.get("email"));
        if (resultEmail.isPresent()) {
            if (!resultEmail.get().getPassword().equals(data.get("pw"))) {
                return new StatusCode(HttpStatus.NOT_FOUND, "로그인 실패! 비밀번호를 확인해주세요.").sendResponse();
            }
            Map<String, String> tokens = jwtTokenProvider.generateToken(resultEmail.get().getEmail());
            return new StatusCode(HttpStatus.OK, tokens, "로그인 성공!").sendResponse();
        }
        return new StatusCode(HttpStatus.NOT_FOUND, "로그인 실패! 로그인 또는 비밀번호를 확인해주세요.").sendResponse();
    }

    public ResponseEntity checkEmail(Map<String, String> email ) {
        Optional<Users> resultEmail = userRepository.findById(email.get("email"));
        if (resultEmail.isPresent()) {
            System.out.println(resultEmail);
            return new StatusCode(HttpStatus.OK, "이메일이 있음").sendResponse();
        } else {
            return new StatusCode(HttpStatus.BAD_REQUEST, "없는 이메일 입니다").sendResponse();
        }
    }

    public ResponseEntity getUserInfo(String token) {
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if (jwtTokenProvider.validateAccessToken(tokenFilter)) { // 인증된 유저

            return new StatusCode(HttpStatus.OK, "유저 정보 조회 성공").sendResponse();
        } else { // 누규..?
            return new StatusCode(HttpStatus.BAD_REQUEST, "이미 만료된 유저임").sendResponse();
        }
    }

    public ResponseEntity register(Users user) {
        try {
            // 이메일 중복 검사
            if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
                return new StatusCode(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다.").sendResponse();
            }

            // 존재하지 않는 이메일일시 해당 계정정보 저장
            userRepository.save(user);
            return new StatusCode(HttpStatus.OK, "회원 가입이 완료되었습니다!").sendResponse();
        } catch (Exception e) {
            return new StatusCode(HttpStatus.BAD_REQUEST, "서버에 에러가 발생했습니다.").sendResponse();
        }
    }

    public ResponseEntity getTokenUsedRefreshToken(Map<String, String> data){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Map<String, String> token = jwtTokenProvider.generateAccessToken(data.get("refreshToken"));

        if(token.get("access_token") != null){ // 성공적으로 재발급이 됨
            return new StatusCode(HttpStatus.OK, token, "액세스 토큰 재발급 성공").sendResponse();
        }else{
            return new StatusCode(HttpStatus.INTERNAL_SERVER_ERROR, "리프레쉬 토큰이 만료되었거나, 알 수 없는 에러").sendResponse();
        }
    }

    public ResponseEntity uploadFile(MultipartFile[] multipartFiles){
        return new StatusCode(HttpStatus.OK, "임시").sendResponse();
    }
}