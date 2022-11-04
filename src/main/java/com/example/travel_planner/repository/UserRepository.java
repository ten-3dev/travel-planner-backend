package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, String> {
    List<Users> findByEmail(String email);

    @Query(value = "select id from plans where email = :email", nativeQuery = true)
    List<String> getIdByPlans(String email);

    @Query(value = "delete from likes where id IN (:list)", nativeQuery = true)
    List<String> deletePlanListByLikes(List<String> list);

    @Query(value = "delete from comments where id IN (:list)", nativeQuery = true)
    List<String> deletePlanListByComments(List<String> list);

    @Query(value = "delete from plans where email = :email", nativeQuery = true)
    List<String> deletePlansById(String email);

    @Query(value = "delete from likes where email = :email", nativeQuery = true)
    void deleteLikesByEmail(String email);

    @Query(value = "delete from comments where email = :email", nativeQuery = true)
    void deleteCommentsByEmail(String email);

}