package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.*;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Course;
import com.munan.studentCourseReg.service.StudentService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "Student Controller", description = "Student Controller")
public class StudentController {

    private final StudentService studentService;


    @Operation(summary = "Add Student", description = "Add new Student")
    @PostMapping("/add/newStudent")
    public ResponseEntity<HttpResponse<?>> add(@RequestBody StudentDto studentDto) throws AlreadyExistException, NotFoundException, MessagingException {
        return studentService.addStudent(studentDto);
    }

    @Operation(summary = "Add new Student and new CourseList", description = "Add new student with a new List of courses")
    @PostMapping("/add/courseList")
    public ResponseEntity<HttpResponse<?>> add_student_course(@RequestBody Student_courseDto student_courseDto) throws AlreadyExistException, NotFoundException, MessagingException {

        return studentService.add_student_course(student_courseDto);
    }

    @Operation(summary = "Add CourseList to student", description = "Add existing or new courses to existing student")
    @PostMapping("/add/{student_id}/courses")
    public ResponseEntity<HttpResponse<?>> addCourseList(@PathVariable(value = "student_id") Long student_id, @RequestBody CourseListDto courseList) throws NotFoundException {

        return studentService.add_courseList(student_id, courseList);
    }

    @Operation(summary = "Get all Students", description = "Get List of all students")
    @GetMapping("/get/getAll")
    public ResponseEntity<HttpResponse<?>> getStudents()
    {
        return studentService.getStudents();
    }

    @Operation(summary = "Get all Students of a Course", description = "Get List of all Students belonging to one Course")
    @GetMapping("/get/{course_id}/students")
    public ResponseEntity<HttpResponse<?>> getStudentsByCourse(@PathVariable("course_id") Long course_id) throws NotFoundException {
        return studentService.StudentsByCourse(course_id);
    }

    @Operation(summary = "Delete a Student", description = "Delete single student")
    @DeleteMapping("/delete/student/{student_id}")
    public ResponseEntity<HttpResponse<?>> deleteStudent(@PathVariable("student_id") Long student_id) throws NotFoundException {
        return studentService.deleteStudent(student_id);
    }

    @Operation(summary = "Delete Course from Student", description = "Delete student course")
    @DeleteMapping("/delete/{student_id}/course/{course_id}")
    public ResponseEntity<HttpResponse<?>>
    deleteCourseByStudent(@PathVariable("student_id") Long student_id, @PathVariable("course_id")Long course_id)
            throws NotFoundException {
        return studentService.deleteCourseByStudent(student_id, course_id);
    }

    @Operation(summary = "Update Student", description = "Update Student record")
    @PutMapping("/update/updateStudent")
    public ResponseEntity<HttpResponse<?>> updateStudent(@RequestBody UpdateStudentDto studentDto) throws NotFoundException, AlreadyExistException {
        return studentService.updateStudent(studentDto);
    }

}
