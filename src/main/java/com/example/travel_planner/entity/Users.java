package com.example.travel_planner.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private int tel;
    @Column(length = 50, nullable = false)
    private java.time.LocalDate birth;
}
