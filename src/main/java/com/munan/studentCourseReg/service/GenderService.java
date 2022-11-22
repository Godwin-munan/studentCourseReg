package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Gender;
import com.munan.studentCourseReg.model.Student;
import com.munan.studentCourseReg.repository.GenderRepository;
import com.munan.studentCourseReg.repository.StudentRepository;
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
public class GenderService {

    private final GenderRepository genderRepository;
    private final StudentRepository studentRepository;

    public  ResponseEntity<HttpResponse<?>> updateGender(Gender gender) {

        Gender newGender = new Gender();
        newGender.setType(gender.getType().toLowerCase());
        Gender updatedGender = genderRepository.save(newGender);

        return  ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", updatedGender)
        );
    }


    public ResponseEntity<HttpResponse<?>> addGender(Gender gender) throws AlreadyExistException {

        Optional<Gender> existingGender = genderRepository.findByType(gender.getType());

        if(existingGender.isPresent()){
            throw new AlreadyExistException("Gender already exist");
        }

        Gender newGender = new Gender();
        newGender.setType(gender.getType().toLowerCase());

        Gender savedGender = genderRepository.save(newGender);

        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "successfully added "
                        +savedGender.getType()+" gender", savedGender)
        );
    }

    public ResponseEntity<HttpResponse<?>> getAllGender() {
        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "successfully added ", genderRepository.findAll())
        );
    }

    public ResponseEntity<HttpResponse<?>> deleteGenderById(Long id) throws NotFoundException, AlreadyExistException {

        Optional<Gender> findGender = genderRepository.findById(id);

        if(findGender.isEmpty()){
            throw new NotFoundException("This record does not exist");
        }

        Optional<Student> studentByGender = studentRepository.findByGender_Id(id).stream().findFirst();

        if(studentByGender.isPresent()){
            throw new AlreadyExistException("Can not delete this record, because it is reference in another table");
        }
        genderRepository.deleteById(id);

//        Optional<Gender> findGender1 = genderRepository.findById(id);


        return  ResponseEntity.ok(
           new HttpResponse<>(HttpStatus.OK.value(), "Successful", "record with id "+id+
                   " successfully deleted")
        );

    }
}
