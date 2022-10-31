package com.munan.studentCourseReg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
public class Student_courseDto {

    private String firstName;

    private String lastName;

    private String matric_number;

    private Integer age;

    private String gender;

    private String email;

    private Integer level;

    private String department;

    private ArrayList<CourseDto> courses;

}