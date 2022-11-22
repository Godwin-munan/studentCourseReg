package com.munan.studentCourseReg.repository;

import com.munan.studentCourseReg.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findStudentsByCourses_id(Long courses_id);

//    Optional<Student> findByName(String name);

    Optional<Student> findByFirstName(String name);

    Optional<Student> findByGender_Id(Long id);

    Optional<Student> findByLevel_Id(Long id);

    Optional<Student> findByDepartment_Id(Long id);
}
