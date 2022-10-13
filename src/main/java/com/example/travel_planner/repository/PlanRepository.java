package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Plans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plans, String> {
}
