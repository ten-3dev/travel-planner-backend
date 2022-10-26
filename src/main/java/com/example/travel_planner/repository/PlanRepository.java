package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Plans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plans, String> {
    @Query(value = "select * from plans where email = :email", nativeQuery = true)
    List<Plans> getPlansByEmail(String email);
}
