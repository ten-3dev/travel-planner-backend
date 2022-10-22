package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Likes;
import com.example.travel_planner.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, String> {
    List<Likes> findByEmail(Users email);
}
