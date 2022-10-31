package com.munan.studentCourseReg.repository;

import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long> {


    Optional<Gender> findByType(String type);

}
