package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
  List<Users> findByEmail(String email);
}
