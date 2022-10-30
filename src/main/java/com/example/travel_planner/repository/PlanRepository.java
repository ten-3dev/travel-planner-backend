package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Comments;
import com.example.travel_planner.entity.Plans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plans, String> {
    @Query(value = "select * from plans where email = :email", nativeQuery = true)
    List<Plans> getPlansByEmail(String email);

    @Query(value = "select * from plans where email = :email and id = :id", nativeQuery = true)
    Plans getPlansByEmailAndId(String email, String id);

    @Query(value = "select * from plans where email = :email and type = 1", nativeQuery = true)
    List<Plans> getSharedPlanType(String email); //공유된 플랜 조회

    @Query(value = "delete from plans where id = :idx", nativeQuery = true)
    void deleteByIdx(int idx);

    @Query(value = "delete from comments where id = :idx" , nativeQuery = true)
    void deleteCommentByIdx(int idx);

    @Query(value = "select * from plans where id = :id and type = 1", nativeQuery = true)
    List<Plans> getPlansById(String id);
}
