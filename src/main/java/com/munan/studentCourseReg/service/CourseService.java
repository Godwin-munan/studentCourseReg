package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.dto.CourseDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Course;
import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Student;
import com.munan.studentCourseReg.repository.CourseRepository;
import com.munan.studentCourseReg.repository.DepartmentRepository;
import com.munan.studentCourseReg.repository.StudentRepository;
import com.munan.studentCourseReg.util.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;


    public ResponseEntity<HttpResponse<?>> addCourse(CourseDto courseDto) throws AlreadyExistException, NotFoundException {


        Optional<Course> findCourse = courseRepository.findByName(courseDto.getName());

        if(findCourse.isPresent())
        {
            throw new AlreadyExistException(courseDto.getName()+" already exist");
        }

        Optional<Department> findDept = departmentRepository.findByName(courseDto.getDepartment());

        if(findDept.isEmpty())
        {
            throw new NotFoundException(courseDto.getDepartment()+" faculty does not exist");
        }

        Course newCourse = new Course();
        newCourse.setName(courseDto.getName());
        newCourse.setCode(courseDto.getCode());
        newCourse.setCredit_unit(courseDto.getCredit_unit());
        newCourse.setDepartment(findDept.get());

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", courseRepository.save(newCourse))
        );
    }

    public ResponseEntity<HttpResponse<?>> getCourses(String field, Integer page, Integer size) {

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(),
                        "Successful",
                        courseRepository.findAll(PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, field))))
        );
    }

    public ResponseEntity<HttpResponse<?>> deleteCourse(Long course_id) throws NotFoundException {

        Optional<Course> findCourse = courseRepository.findById(course_id);

        if(findCourse.isEmpty())
        {
            throw new NotFoundException("Course with id "+course_id+" not found");
        }

        String code = findCourse.get().getCode();

        findCourse.get().setDeleted(true);
        courseRepository.deleteById(course_id);

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", code+" has been deleted")
        );
    }

    public ResponseEntity<HttpResponse<?>> getCoursesByStudent(Long student_id) throws NotFoundException {

        Optional<Student> findStudent = studentRepository.findById(student_id);

        if(findStudent.isEmpty())
        {
            throw new NotFoundException("Student with id "+student_id+" not found");
        }

        List<Course> courses = courseRepository.findCoursesByStudentsId(student_id);

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", courses)
        );
    }
}
