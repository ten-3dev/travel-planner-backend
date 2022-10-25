package com.example.travel_planner.service;

import com.example.travel_planner.config.JwtTokenProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.dto.PlanDTO;
import com.example.travel_planner.entity.Plans;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.PlanRepository;
import com.example.travel_planner.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.catalina.User;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            System.out.println("plan = " + plan);

            Users user = Users.builder().email(plan.get("email")).build();
            // DB에 있는 아이디랑 plan.get("email") 동일할때
            Plans plans = Plans.builder()
                    .email(user)
                    .build();


            //planRepository.save(plan);
            return new StatusCode(HttpStatus.OK, "플랜 생성이 완료되었습니다!").sendResponse();
        }catch (Exception e){
            return new StatusCode(HttpStatus.BAD_REQUEST, "서버에 에러가 발생했습니다.").sendResponse();
        }
    }
}

