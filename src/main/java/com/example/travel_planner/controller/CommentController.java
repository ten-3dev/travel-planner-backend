package com.example.travel_planner.controller;

import com.example.travel_planner.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class CommentController {
    @Autowired
    CommentService commentService;
}
