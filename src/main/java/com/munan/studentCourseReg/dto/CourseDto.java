package com.munan.studentCourseReg.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CourseDto {

    private String name;

    private String code;

    private Integer credit_unit;

    private String department;
}
