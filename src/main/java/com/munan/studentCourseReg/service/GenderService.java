package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Gender;
import com.munan.studentCourseReg.model.Student;
import com.munan.studentCourseReg.repository.GenderRepository;
import com.munan.studentCourseReg.repository.StudentRepository;
import com.munan.studentCourseReg.util.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Optional;

import static com.munan.studentCourseReg.constants.URI_Constant.getURL;

@Service
@Transactional
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;
    private final StudentRepository studentRepository;

    private static final String baseRoute = "/api/gender";

    private Logger logger = LoggerFactory.getLogger(GenderService.class);

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

        URI uri = getURL(baseRoute+"/add/addGender");

        return ResponseEntity.created(uri).body(
                new HttpResponse<>(HttpStatus.CREATED.value(), "successfully added "
                        +gender.getType()+" gender", genderRepository.save(newGender))
        );
    }

    public ResponseEntity<HttpResponse<?>> getAllGender() {
        logger.info("Successful");

        return ResponseEntity.ok(
                new HttpResponse<>(
                        HttpStatus.OK.value(),
                        "successful", genderRepository.findAll())
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


//        URI uri = getURL(baseRoute+"/delete/deleteById/"+id);

        return  ResponseEntity.ok(
           new HttpResponse<>(HttpStatus.OK.value(), "Successful", "record with id "+id+
                   " successfully deleted")
        );

    }

}
