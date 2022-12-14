package com.munan.studentCourseReg.repository;

import com.munan.studentCourseReg.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    Optional<Department> findByFaculty_id(Long id);
}
