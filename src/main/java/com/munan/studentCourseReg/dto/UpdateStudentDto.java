package com.munan.studentCourseReg.dto;


import lombok.*;

@NoArgsConstructor
@Data
public class UpdateStudentDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String matric_number;

    private Integer age;

    private String gender;

    private String email;

    private Integer level;

    private String department;
}
