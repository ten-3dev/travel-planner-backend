package com.example.travel_planner.repository;

import com.example.travel_planner.entity.Likes;
import com.example.travel_planner.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, String> {

    @Query(value = "SELECT * FROM likes WHERE email = :email AND type = :type ORDER BY like_idx DESC LIMIT :offset, 4;", nativeQuery = true)
    List<Likes> selectLikeByEmail(String email, String type, int offset);

    @Query(value = "select * from likes where email = :email", nativeQuery = true)
    List<Likes> selectLikeByEmail(String email);

    @Query(value = "select * from likes where email = :email and id = :id", nativeQuery = true)
    Likes findByIdAndEmail(String email, String id);

    @Query(value = "delete from likes where like_idx = :idx", nativeQuery = true)
    void deleteByIdx(int idx);

    @Query(value = "SELECT COUNT(*) FROM likes WHERE email = :email and type = :type", nativeQuery = true)
    int selectLikeCount(String email, String type);

    @Query(value = "delete from likes where id = :id and type = :type", nativeQuery = true)
    void deleteByIdAndType(int id, String type);
}
