package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.dto.CourseDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Course;
import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.repository.CourseRepository;
import com.munan.studentCourseReg.repository.DepartmentRepository;
import com.munan.studentCourseReg.util.HttpResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final DepartmentRepository departmentRepository;


    public ResponseEntity<HttpResponse> addCourse(CourseDto courseDto) throws AlreadyExistException, NotFoundException {


        Optional<Course> findCourse = courseRepository.findByName(courseDto.getName());

        if(findCourse.isPresent())
        {
            throw new AlreadyExistException(courseDto.getName()+" already exist");
        }

        Optional<Department> findDept = departmentRepository.findByName(courseDto.getDepartment());

        if(!findDept.isPresent())
        {
            throw new NotFoundException(courseDto.getDepartment()+" faculty does not exist");
        }

        Course newCourse = new Course();
        newCourse.setName(courseDto.getName());
        newCourse.setCode(courseDto.getCode());
        newCourse.setCredit_unit(courseDto.getCredit_unit());
        newCourse.setDepartment(findDept.get());

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", courseRepository.save(newCourse))
        );
    }

    public ResponseEntity<HttpResponse> getCourses() {

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", courseRepository.findAll())
        );
    }

    public ResponseEntity<HttpResponse> deleteCourse(Long course_id) throws NotFoundException {

        Optional<Course> findCourse = courseRepository.findById(course_id);

        if(!findCourse.isPresent())
        {
            throw new NotFoundException("Course with id "+course_id+" not found");
        }

        String code = findCourse.get().getCode();

        findCourse.get().setDeleted(true);
        courseRepository.save(findCourse.get());
        courseRepository.deleteById(course_id);

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", code+" has been deleted")
        );
    }
}
