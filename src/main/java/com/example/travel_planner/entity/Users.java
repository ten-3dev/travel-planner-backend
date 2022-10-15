package com.example.travel_planner.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity //JPA로 관리되는 엔티티객체. 즉 테이블
@Table
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor //빈생성자
public class Users {
    @Id
    private String email;
    @Column(length = 50, nullable = false)
    private String password;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 50)
    private String profileImg;
    @Column(length = 12, nullable = false)
    private String tel;
    @Column(nullable = false)
    private java.time.LocalDate birth;
}
