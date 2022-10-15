package com.example.travel_planner.service;

import com.example.travel_planner.dto.PlanDTO;
import com.example.travel_planner.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;
}
