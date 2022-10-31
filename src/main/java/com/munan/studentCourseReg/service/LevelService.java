package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Gender;
import com.munan.studentCourseReg.model.Level;
import com.munan.studentCourseReg.repository.LevelRepository;
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
public class LevelService {

    private final LevelRepository levelRepository;

    public ResponseEntity<HttpResponse> addLevel(Level level) throws AlreadyExistException {

        Optional<Level> existingLevel = levelRepository.findByLevelNumber(level.getLevelNumber());

        if(existingLevel.isPresent())
        {
            throw new AlreadyExistException("Level already exist");
        }


        Level savedLevel = levelRepository.save(level);

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "successful", savedLevel)
        );
    }

    public ResponseEntity<HttpResponse> getAllLevels() {

        return ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "successfully added ", levelRepository.findAll())
        );
    }

    public ResponseEntity<HttpResponse> deleteLevelById(Long id) throws NotFoundException {

        Optional<Level> findLevel = levelRepository.findById(id);

        if(!findLevel.isPresent()){
            throw new NotFoundException("This record does not exist");
        }

        levelRepository.delete(findLevel.get());

        return  ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successful", "record with id "+id+
                        " successfully deleted")
        );
    }

    public ResponseEntity<HttpResponse> updateLevel(Level level) {

        return  ResponseEntity.ok(
                new HttpResponse(HttpStatus.OK.value(), "Successfull", levelRepository.save(level))
        );
    }
}
