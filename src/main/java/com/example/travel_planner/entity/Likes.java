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
    @ManyToOne
    @JoinColumn(name ="email", nullable = false)
    private Users email;

    @Id // 임시방편
    private String type;

    @ManyToOne
    @JoinColumn(name ="id", nullable = false)
    private Plans id;
}
