package com.munan.studentCourseReg.dto;

import lombok.*;

@NoArgsConstructor
@Data
public class UpdateCourseDto {

    private Long id;

    private String name;

    private String code;

    private Integer credit_unit;

    private String department;
}
