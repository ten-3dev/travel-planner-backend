package com.example.travel_planner.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LikesRepositoryTests {
    @Autowired
    LikeRepository likeRepository;

    @Test
    public void testClass(){
        System.out.println(likeRepository.getClass().getName());
    }
}
