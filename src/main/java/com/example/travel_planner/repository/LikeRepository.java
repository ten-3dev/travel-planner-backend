package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, String> {
}
