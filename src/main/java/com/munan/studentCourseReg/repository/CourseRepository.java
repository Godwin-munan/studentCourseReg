package com.munan.studentCourseReg.repository;

import com.munan.studentCourseReg.dto.CourseDto;
import com.munan.studentCourseReg.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);

//    List<Course> findCoursesByStudentsId(Long tutorialId);

    List<Course> findCoursesByStudentsId(Long students_id);

    Optional<Course> findByCode(String name);
}
