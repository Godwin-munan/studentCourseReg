package com.munan.studentCourseReg.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "gender")
@SQLDelete(sql = "UPDATE gender SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
@Getter
@Setter
public class Gender implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private boolean deleted = Boolean.FALSE;
}
