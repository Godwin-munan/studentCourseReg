package com.munan.studentCourseReg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
public class CourseListDto {

    private ArrayList<CourseDto> courses;
}
