package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.GlobalExceptionHandling;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.model.Gender;
import com.munan.studentCourseReg.service.GenderService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gender")
@Tag(name = "Gender Controller", description = "Gender Controller")
@AllArgsConstructor
public class GenderController extends GlobalExceptionHandling {

    private final GenderService genderService;

    @Operation(summary = "Add Gender", description = "Add a new gender")
    @PostMapping("/addGender")
    public ResponseEntity<HttpResponse> add(@RequestBody Gender gender) throws AlreadyExistException {

        return genderService.addGender(gender);
    }

    @Operation(summary = "Get all genders", description = "Get all existing genders")
    @GetMapping("/getAll")
    public ResponseEntity<HttpResponse> getAll(){ return genderService.getAllGender();}

    @Operation(summary = "Delete existing gender", description = "Delete existing gender by id")
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("id") Long id) throws NotFoundException { return genderService.deleteGenderById(id);}

    @Operation(summary = "Update Gender")
    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateGender(@RequestBody Gender gender) {
        return genderService.updateGender(gender);
    }
}
