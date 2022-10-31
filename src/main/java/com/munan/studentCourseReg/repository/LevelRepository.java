package com.munan.studentCourseReg.repository;

import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findByLevelNumber(Integer levelNumber);

}
