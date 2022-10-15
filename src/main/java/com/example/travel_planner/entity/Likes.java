package com.example.travel_planner.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeidx;

    @ManyToOne
    @JoinColumn(name ="email", nullable = false)
    private Users email;

    @Column(length = 50, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name ="id", nullable = false)
    private Plans id;
}
