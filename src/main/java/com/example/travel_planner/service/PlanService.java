package com.example.travel_planner.service;

import com.example.travel_planner.config.JwtTokenProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Plans;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.PlanRepository;
import com.example.travel_planner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity createPlan(Map<String, String> plan) {
        try{
            List<Users> list = userRepository.findByEmail(plan.get("email"));
            for (Users users : list){
                Plans plans = Plans.builder()
                        .email(users)
                        .title(plan.get("title"))
                        .plan(plan.get("plan"))
                        .date(plan.get("date"))
                        .type(Integer.parseInt(plan.get("type")))
                        .build();
                planRepository.save(plans);
            }
            return new StatusCode(HttpStatus.OK, "플랜 생성이 완료되었습니다!").sendResponse();
        }catch (Exception e){
            return new StatusCode(HttpStatus.BAD_REQUEST, "서버에 에러가 발생했습니다.").sendResponse();
        }
    }

    public ResponseEntity getUserPlan(String token){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if (jwtTokenProvider.validateAccessToken(tokenFilter)) {
            String getUserEmailFromToken = jwtTokenProvider.getUserEmailFromToken(tokenFilter);

            List<Plans> resultPlans = planRepository.getPlansByEmail(getUserEmailFromToken);

            System.out.println("resultPlans = " + resultPlans);

            return new StatusCode(HttpStatus.OK, resultPlans, "유저 플랜 조회 성공").sendResponse();
        } else {
            return new StatusCode(HttpStatus.UNAUTHORIZED, "이미 만료된 유저임").sendResponse();
        }
    }

    @Transactional
    public ResponseEntity updateSharePlan(String token, Map<String, String> data){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if (jwtTokenProvider.validateAccessToken(tokenFilter)) {
            String getUserEmailFromToken = jwtTokenProvider.getUserEmailFromToken(tokenFilter);

            Optional<Users> user = userRepository.findById(getUserEmailFromToken);
            Plans resultPlan = planRepository.getPlansByEmailAndId(getUserEmailFromToken, data.get("id"));

            if(resultPlan == null){
                return new StatusCode(HttpStatus.BAD_REQUEST, "알 수 없는 오류").sendResponse();
            }else {
                Plans plans = Plans.builder()
                        .id(resultPlan.getId())
                        .email(user.get())
                        .title(resultPlan.getTitle())
                        .plan(resultPlan.getPlan())
                        .date(resultPlan.getDate())
                        .type(resultPlan.getType() == 0 ? 1 : 0)
                        .build();
                planRepository.save(plans);
                return new StatusCode(HttpStatus.OK, "유저 비/공개 변경 성공").sendResponse();
            }
        } else {
            return new StatusCode(HttpStatus.UNAUTHORIZED, "만료된 토큰").sendResponse();
        }
    }

}

