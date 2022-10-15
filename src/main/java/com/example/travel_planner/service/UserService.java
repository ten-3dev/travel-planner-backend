package com.example.travel_planner.service;

import com.example.travel_planner.dto.UserDTO;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.UserRepository;
import com.example.travel_planner.service.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUserInfoKakao(String code){
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = "https://kauth.kakao.com/oauth/token";

            String client_id = "0a61f9efbdac3933e6a14ed6f553bd00";
            String redirect_uri = "http://localhost:8080/kakaoCallback";
            String client_secret = "K2uqygqk3ddG8UFgrIFdE76bKg9SpEwT";

            String params = String.format("?grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s&client_secret=%s", client_id, redirect_uri, code, client_secret);

            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + params).build();

            //이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

        } catch (Exception e) {
            result.put("statusCode", "500");
            result.put("body"  , e.toString());
        }

        return result;
    }

    public Map<String, String> login(Map<String, String> data){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Optional<Users> result = userRepository.findById(data.get("email"));
        if(result.isPresent()){
            Users user = result.get();
            Map<String, String> tokens = jwtTokenProvider.generateToken(user.getEmail());
            return tokens;
        }
        return null;
    }
}
