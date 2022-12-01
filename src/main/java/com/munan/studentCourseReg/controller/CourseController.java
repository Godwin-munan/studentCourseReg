package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.CourseDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.service.CourseService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Tag(name = "Course Controller", description = "Course Controller")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Add Course", description = "Add new course")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse<?>> add(@RequestBody CourseDto courseDto) throws AlreadyExistException, NotFoundException {
        return courseService.addCourse(courseDto);
    }

    @Operation(summary = "Get Course", description = "Get all courses")
    @GetMapping("/get/getAll/{page}/{size}/{field}")
    public ResponseEntity<HttpResponse<?>> getCourses(@PathVariable("page") Integer page,
                                                      @PathVariable("size") Integer size,
                                                      @PathVariable("field") String field) {
        return courseService.getCourses(field, page, size);
    }

    @Operation(summary = "Get all Courses of a Student", description = "Get List of all courses belonging to one student")
    @GetMapping("/get/{student_id}/courses")
    public ResponseEntity<HttpResponse<?>> getCoursesByStudent(@PathVariable("student_id") Long student_id) throws NotFoundException {

        return courseService.getCoursesByStudent(student_id);
    }

    @Operation(summary = "Delete Course", description = "Delete Single Course by Id")
    @DeleteMapping("/delete/{course_id}")
    public ResponseEntity<HttpResponse<?>> deleteCourse(@PathVariable("course_id") Long course_id) throws NotFoundException {
        return courseService. deleteCourse(course_id);
    }

}
