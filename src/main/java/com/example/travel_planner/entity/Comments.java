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
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentidx;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Users email;

    @Column(columnDefinition = "longtext", nullable = false)
    private String content;

    @Column(nullable = false)
    private java.time.LocalDate date;

    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private String type;


}
