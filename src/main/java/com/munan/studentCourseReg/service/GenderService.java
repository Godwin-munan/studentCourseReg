package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.model.Gender;
import com.munan.studentCourseReg.repository.GenderRepository;
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
public class GenderService {

    private final GenderRepository genderRepository;

    public  ResponseEntity<HttpResponse> updateGender(Gender gender) {

        Gender newGender = new Gender();
        newGender.setType(gender.getType().toLowerCase());
        Gender updatedGender = genderRepository.save(newGender);

        return  ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successfull", updatedGender)
        );
    }


    public ResponseEntity<HttpResponse> addGender(Gender gender) throws AlreadyExistException {

        Optional<Gender> existingGender = genderRepository.findByType(gender.getType());

        if(existingGender.isPresent()){
            throw new AlreadyExistException("Gender already exist");
        }

        Gender newGender = new Gender();
        newGender.setType(gender.getType().toLowerCase());

        Gender savedGender = genderRepository.save(newGender);

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "successfully added "
                        +savedGender.getType()+" gender", savedGender)
        );
    }

    public ResponseEntity<HttpResponse> getAllGender() {
        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "successfully added ", genderRepository.findAll())
        );
    }

    public ResponseEntity<HttpResponse> deleteGenderById(Long id) throws NotFoundException {

        Optional<Gender> findGender = genderRepository.findById(id);

        if(!findGender.isPresent()){
            throw new NotFoundException("This record does not exist");
        }

        genderRepository.delete(findGender.get());

        return  ResponseEntity.ok(
           new HttpResponse(HttpStatus.OK.value(), "record with id "+id+
                   " successfully deleted", null)
        );

    }
}
