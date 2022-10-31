package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.CourseDto;
import com.munan.studentCourseReg.dto.DeptDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.service.CourseService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@AllArgsConstructor
@Tag(name = "Course Controller", description = "Course Controller")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Add Course", description = "Add new course")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse> add(@RequestBody CourseDto courseDto) throws AlreadyExistException, NotFoundException {
        return courseService.addCourse(courseDto);
    }

    @Operation(summary = "Get Course", description = "Get all courses")
    @GetMapping("/getAll")
    public ResponseEntity<HttpResponse> getCourses()
    {
        return courseService.getCourses();
    }

    @Operation(summary = "Delete Course", description = "Delete Single Course by Id")
    @DeleteMapping("/delete/{course_id}")
    public ResponseEntity<HttpResponse> deleteCourse(@PathVariable("course_id") Long course_id) throws NotFoundException {
        return courseService. deleteCourse(course_id);
    }

}
