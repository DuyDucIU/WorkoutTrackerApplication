package com.duyduc.workout_tracker.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
}
