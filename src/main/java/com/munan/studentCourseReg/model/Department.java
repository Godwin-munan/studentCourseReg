package com.munan.studentCourseReg.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "department")
@SQLDelete(sql = "UPDATE department SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
@Getter
@Setter
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Faculty faculty;

    private boolean deleted = Boolean.FALSE;

}
