package com.example.travel_planner.service;

import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository; //DI

    @Transactional
    public int 회원가입(Users user) {
        try {
            userRepository.save(user);
            return 1;
        } catch (Exception e) {
            e.getMessage();
            return -1;
        }
    }
}
