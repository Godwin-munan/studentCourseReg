package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.dto.DeptDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.repository.DepartmentRepository;
import com.munan.studentCourseReg.repository.FacultyRepository;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;


    public ResponseEntity<HttpResponse> addDept(DeptDto deptDto) throws AlreadyExistException, NotFoundException {

        Optional<Department> findDept = departmentRepository.findByName(deptDto.getName());

        if(findDept.isPresent())
        {
            throw new AlreadyExistException(deptDto.getName()+" already exist");
        }

        Optional<Faculty> findFaculty = facultyRepository.findByName(deptDto.getFaculty());

        if(!findFaculty.isPresent())
        {
            throw new NotFoundException(deptDto.getFaculty()+" faculty does not exist");
        }

        Department newDept = new Department();
        newDept.setName(deptDto.getName());
        newDept.setFaculty(findFaculty.get());

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", departmentRepository.save(newDept))
        );
    }

    public ResponseEntity<HttpResponse> getAllDepartments() {

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", departmentRepository.findAll())
        );
    }

    public ResponseEntity<HttpResponse> getDeptById(Long id) throws NotFoundException {

        Optional<Department> findDept = departmentRepository.findById(id);

        if(!findDept.isPresent())
        {
            throw new NotFoundException("Record for id "+id+" is not found");
        }

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", findDept.get())
        );
    }

    public ResponseEntity<HttpResponse> deleteDeptById(Long id) throws NotFoundException {

        Optional<Department> findDept = departmentRepository.findById(id);

        if(!findDept.isPresent())
        {
            throw new NotFoundException("Record for id "+id+" does not exist");
        }

        String name = findDept.get().getName();
        departmentRepository.delete(findDept.get());

        return  ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", name+" department successfully deleted")
        );
    }

    public ResponseEntity<HttpResponse> updateDept(DeptDto deptDto)
    {
        Optional<Faculty> findDept = facultyRepository.findByName(deptDto.getFaculty());

        Department updateDept = new Department();
        updateDept.setId(deptDto.getId());
        updateDept.setName(deptDto.getName());
        updateDept.setFaculty(findDept.get());

        return  ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", departmentRepository.save(updateDept))
        );
    }
}
