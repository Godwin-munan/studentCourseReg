package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Department;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.repository.DepartmentRepository;
import com.munan.studentCourseReg.repository.FacultyRepository;
import com.munan.studentCourseReg.util.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    public ResponseEntity<HttpResponse<?>> addFaculty(Faculty faculty) throws AlreadyExistException {

        Optional<Faculty> findFaculty = facultyRepository.findByName(faculty.getName());

        if(findFaculty.isPresent())
        {
            throw new AlreadyExistException("Faculty with name "+findFaculty.get().getName()+" already exist");
        }

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Faculty successfully added", facultyRepository.save(faculty))
        );

    }

    public ResponseEntity<HttpResponse<?>> getAllFaculties() {

        return ResponseEntity.ok(
            new HttpResponse<>(HttpStatus.OK.value(), "Successful", facultyRepository.findAll())
        );
    }

    public ResponseEntity<HttpResponse<?>> getFacultyById(Long id) throws NotFoundException {

        Optional<Faculty> findFaculty = facultyRepository.findById(id);

        if(findFaculty.isEmpty())
        {
            throw new NotFoundException("Record for id "+id+" is not found");
        }

        return ResponseEntity.ok(
            new HttpResponse<>(HttpStatus.OK.value(), "Successful", findFaculty.get())
        );
    }


    public ResponseEntity<HttpResponse<?>> deleteFacultyById(Long id) throws NotFoundException, AlreadyExistException {

        Optional<Faculty> findFaculty = facultyRepository.findById(id);

        if(findFaculty.isEmpty())
        {
            throw new NotFoundException("Record with id "+id+" does not exist");
        }

        Optional<Department> deptByFaculty = departmentRepository.findByFaculty_id(id).stream().findFirst();

        if(deptByFaculty.isPresent()){
            throw new AlreadyExistException("Can not delete this record, because it is reference in another table");
        }

        String name = findFaculty.get().getName();
        facultyRepository.delete(findFaculty.get());

        return  ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", name+" faculty successfully deleted")
        );
    }

    public ResponseEntity<HttpResponse<?>> updateFaculty(Faculty faculty) {

        Faculty updatedFaculty = facultyRepository.save(faculty);

        return  ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", updatedFaculty)
        );
    }
}

