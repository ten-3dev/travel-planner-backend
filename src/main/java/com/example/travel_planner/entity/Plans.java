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


public class Plans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Users email;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "longtext", nullable = false)
    private String plan;

    @Column(columnDefinition = "TINYINT", length=1, nullable = false)
    private int type;

    @Column(length = 50, nullable = false)
    private String date;

    @Setter
    @Getter
    @Transient
    private int likeCount;
}
