package com.munan.studentCourseReg.model;


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
@Table(name = "student")
@SQLDelete(sql = "UPDATE student SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
@Getter
@Setter
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String matric_number;

    private Integer age;

    private String email;

    @ManyToOne
    private Gender gender;

    @ManyToOne
    private Level level;

    @ManyToOne
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade =  {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "student_course",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") })
    private Set<Course> courses = new HashSet<>();

    private boolean deleted = Boolean.FALSE;


    public void addCourse(Course course_) {
        this.courses.add(course_);
        course_.getStudents().add(this);
    }

    public void removeCourse(long courseId) {
        Course _course = this.courses.stream().filter(c -> c.getId() == courseId).findFirst().orElse(null);
        if (_course != null) {
            this.courses.remove(_course);
            _course.getStudents().remove(this);
        }
    }



}
