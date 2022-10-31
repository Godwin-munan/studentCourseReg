package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.CourseDto;
import com.munan.studentCourseReg.dto.CourseListDto;
import com.munan.studentCourseReg.dto.StudentDto;
import com.munan.studentCourseReg.dto.Student_courseDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Course;
import com.munan.studentCourseReg.service.StudentService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
@Tag(name = "Student Controller", description = "Student Controller")
public class StudentController {

    private final StudentService studentService;


    @Operation(summary = "Add Student", description = "Add new Student")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse> add(@RequestBody StudentDto studentDto) throws AlreadyExistException, NotFoundException, MessagingException {
        return studentService.addStudent(studentDto);
    }

    @Operation(summary = "Add new Student and new CourseList", description = "Add new student with a new List of courses")
    @PostMapping("/students/courseList")
    public ResponseEntity<HttpResponse> add_student_course(@RequestBody Student_courseDto student_courseDto) throws AlreadyExistException, NotFoundException, MessagingException {

        return studentService.add_student_course(student_courseDto);
    }

    @Operation(summary = "Add CourseList to student", description = "Add existing or new courses to existing student")
    @PostMapping("/student/{student_id}/courses")
    public ResponseEntity<HttpResponse> addCourseList(@PathVariable(value = "student_id") Long student_id, @RequestBody CourseListDto courseList) throws NotFoundException {

        return studentService.add_courseList(student_id, courseList);
    }

    @Operation(summary = "Get all Students", description = "Get List of all students")
    @GetMapping("/getAll")
    public ResponseEntity<HttpResponse> getStudents()
    {
        return studentService.getStudents();
    }

    @Operation(summary = "Get all Courses of a Student", description = "Get List of all courses belonging to one student")
    @GetMapping("/student/{student_id}/courses")
    public ResponseEntity<HttpResponse> getCoursesByStudent(@PathVariable("student_id") Long student_id) throws NotFoundException {
        return studentService.getCoursesByStudent(student_id);
    }

    @Operation(summary = "Delete a Student", description = "Delete single student")
    @DeleteMapping("/student/{student_id}")
    public ResponseEntity<HttpResponse> deleteStudent(@PathVariable("student_id") Long student_id) throws NotFoundException {
        return studentService.deleteStudent(student_id);
    }

    @Operation(summary = "Delete Course from Student", description = "Delete student course")
    @DeleteMapping("/student/{student_id}/course/{course_id}")
    public ResponseEntity<HttpResponse>
    deleteCourseByStudent(@PathVariable("student_id") Long student_id, @PathVariable("course_id")Long course_id)
            throws NotFoundException {
        return studentService.deleteCourseByStudent(student_id, course_id);
    }

}
