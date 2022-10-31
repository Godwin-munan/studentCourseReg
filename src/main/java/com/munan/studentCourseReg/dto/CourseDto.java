package com.munan.studentCourseReg.dto;

import com.munan.studentCourseReg.model.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;

@NoArgsConstructor
@Getter
@Setter
public class CourseDto {

    private String name;

    private String code;

    private Integer credit_unit;

    private String department;
}
