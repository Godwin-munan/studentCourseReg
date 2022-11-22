package com.munan.studentCourseReg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@SQLDelete(sql = "UPDATE course SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
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

        @ManyToMany(fetch = FetchType.LAZY,
                cascade = {
                        CascadeType.ALL
                },
                mappedBy = "courses")
        @JsonIgnore
        private Set<Student> students = new HashSet<>();

        private boolean deleted = Boolean.FALSE;
}

