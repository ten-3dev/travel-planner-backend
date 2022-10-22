package com.example.travel_planner.service;

import com.example.travel_planner.config.JwtTokenProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Comments;
import com.example.travel_planner.entity.Likes;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.CommentRepository;
import com.example.travel_planner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity addComment(String token, Map<String, String> data){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if(jwtTokenProvider.validateAccessToken(tokenFilter)){
            Optional<Users> user = userRepository.findById(jwtTokenProvider.getUserEmailFromToken(tokenFilter));
            Comments comments = Comments.builder()
                    .id(data.get("id"))
                    .content(data.get("content"))
                    .date(user.get().getBirth())
                    .type(data.get("type"))
                    .email(user.get())
                    .build();
            commentRepository.save(comments);
            return new StatusCode(HttpStatus.OK, "댓글 추가 성공").sendResponse();
        }else{
            return new StatusCode(HttpStatus.UNAUTHORIZED, "만료된 토큰").sendResponse();
        }
    }

    public ResponseEntity getComment(String token){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if(jwtTokenProvider.validateAccessToken(tokenFilter)){
            List<Comments> Comments = commentRepository.findAll();
            return new StatusCode(HttpStatus.OK, Comments, "좋아요 조회 성공").sendResponse();
        }else{
            return new StatusCode(HttpStatus.UNAUTHORIZED, "만료된 토큰").sendResponse();
        }
    }
}
