package com.example.travel_planner.service;

import com.example.travel_planner.dto.LikeDTO;
import com.example.travel_planner.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
}
