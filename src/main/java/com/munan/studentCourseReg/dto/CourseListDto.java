package com.munan.studentCourseReg.dto;

import lombok.*;

import java.util.ArrayList;

@NoArgsConstructor
@Data
public class CourseListDto {

    private ArrayList<CourseDto> courses;
}
