package com.munan.studentCourseReg.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

    private String email;
    private String password;

}
