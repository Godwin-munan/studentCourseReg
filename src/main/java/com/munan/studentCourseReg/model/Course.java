package com.munan.studentCourseReg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@NoArgsConstructor
@Getter
@Setter
public class Course implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private String code;

        private Integer credit_unit;

        @ManyToOne
        private Department department;

        @ManyToMany(fetch = FetchType.EAGER,
                cascade = {
                        CascadeType.PERSIST,
                        CascadeType.MERGE
                },
                mappedBy = "courses")
        @JsonIgnore
        private Set<Student> students = new HashSet<>();
}

