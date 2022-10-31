package com.munan.studentCourseReg.repository;

import com.munan.studentCourseReg.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentsByCourses_id(Long courses_id);

//    Optional<Student> findByName(String name);

    Optional<Student> findByFirstName(String name);

}
