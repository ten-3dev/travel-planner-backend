package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
public class LikesRepositoryTests {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void testClass(){
        System.out.println(likeRepository.getClass().getName());
    }

    @Transactional
    @Test
    public void getLikes(){
        Optional<Users> user = userRepository.findById("test@test.com");
        System.out.println(user);
    }
}
