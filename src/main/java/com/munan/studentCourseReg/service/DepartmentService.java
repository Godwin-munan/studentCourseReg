package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.dto.DeptDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Course;
import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.model.Student;
import com.munan.studentCourseReg.repository.CourseRepository;
import com.munan.studentCourseReg.repository.DepartmentRepository;
import com.munan.studentCourseReg.repository.FacultyRepository;
import com.munan.studentCourseReg.repository.StudentRepository;
import com.munan.studentCourseReg.util.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;


    public ResponseEntity<HttpResponse<?>> addDept(DeptDto deptDto) throws AlreadyExistException, NotFoundException {

        Optional<Department> findDept = departmentRepository.findByName(deptDto.getName());

        if(findDept.isPresent())
        {
            throw new AlreadyExistException(deptDto.getName()+" already exist");
        }

        Optional<Faculty> findFaculty = facultyRepository.findByName(deptDto.getFaculty());

        if(findFaculty.isEmpty())
        {
            throw new NotFoundException(deptDto.getFaculty()+" faculty does not exist");
        }

        Department newDept = new Department();
        newDept.setName(deptDto.getName());
        newDept.setFaculty(findFaculty.get());

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", departmentRepository.save(newDept))
        );
    }

    public ResponseEntity<HttpResponse<?>> getAllDepartments(Integer page, Integer size, String field) {

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(),
                        "Successful",
                        departmentRepository.findAll(PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, field))))
        );
    }

    public ResponseEntity<HttpResponse<?>> getDeptById(Long id) throws NotFoundException {

        Optional<Department> findDept = departmentRepository.findById(id);

        if(findDept.isEmpty())
        {
            throw new NotFoundException("Record for id "+id+" is not found");
        }

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", findDept.get())
        );
    }

    public ResponseEntity<HttpResponse<?>> deleteDeptById(Long id) throws NotFoundException, AlreadyExistException {

        Optional<Department> findDept = departmentRepository.findById(id);

        if(findDept.isEmpty())
        {
            throw new NotFoundException("Record for id "+id+" does not exist");
        }

        Optional<Student> studentByDept = studentRepository.findByDepartment_Id(id).stream().findFirst();
        Optional<Course>  courseByDept = courseRepository.findByDepartment_id(id).stream().findFirst();

        if(studentByDept.isPresent() || courseByDept.isPresent()){
            throw new AlreadyExistException("Can not delete this record, because it is reference in another table");
        }
        String name = findDept.get().getName();
        departmentRepository.delete(findDept.get());

        return  ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", name+" department successfully deleted")
        );
    }

    public ResponseEntity<HttpResponse<?>> updateDept(DeptDto deptDto)
    {
        Optional<Faculty> findDept = facultyRepository.findByName(deptDto.getFaculty());

        Department updateDept = new Department();
        updateDept.setId(deptDto.getId());
        updateDept.setName(deptDto.getName());
        updateDept.setFaculty(findDept.get());

        return  ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", departmentRepository.save(updateDept))
        );
    }
}
