package com.example.travel_planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data/*@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode 어노테이션을 한꺼번에 설정해주는 어노테이션*/
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
    private String type;
    private String email;
    private int id;
}
