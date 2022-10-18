package com.example.travel_planner.config;

import com.sun.tools.jconsole.JConsoleContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {
    static final String JWT_SECRET_REFRESH = "asdfs!sdjch8s55/@!!fuck";
    static final String JWT_SECRET_ACCESS = "as_dfaskh!~sdf@SCADFA#211!@!@$";

    public Map<String, String> generateToken(String email){
        Map<String, String> result = new HashMap<>();
        // 토큰 유효시간
        int JWT_EXPIRATION_REFRESH = 604800000; // 리프레쉬 (7d)
        int JWT_EXPIRATION_ACCESS = 3600000; // 액세스 (1h)

        String refresh_token = Jwts.builder()
                .setSubject(email) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(new Date(new Date().getTime() + JWT_EXPIRATION_REFRESH)) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_REFRESH) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
        String access_token = Jwts.builder()
                .setSubject(email) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(new Date(new Date().getTime() + JWT_EXPIRATION_ACCESS)) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_ACCESS) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        result.put("refresh_token", refresh_token);
        result.put("access_token", access_token);
        return result;
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(JWT_SECRET_ACCESS).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}

