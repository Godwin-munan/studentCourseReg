package com.munan.studentCourseReg.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class StudentDto {

    private String firstName;

    private String lastName;

    private String matric_number;

    private Integer age;

    private String password;

    private String gender;

    private String email;

    private Integer level;

    private String department;


}
