package com.munan.studentCourseReg.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Data
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