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


public class Plan {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Users email;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "longtext", nullable = false)
    private String plan;

    @Column(columnDefinition = "TINYINT", length=1, nullable = false)
    private String type;

    @Column(length = 50, nullable = false)
    private java.time.LocalDate date;

}
