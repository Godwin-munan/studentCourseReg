package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Faculty;
import com.munan.studentCourseReg.service.FacultyService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty")
@AllArgsConstructor
@Tag(name = "Faculty Controller", description = "Faculty Controller")
public class FacultyController {

    private final FacultyService facultyService;

    @Operation(summary = "Add Faculty", description = "Add Faculty")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse> add(@RequestBody Faculty faculty) throws AlreadyExistException {
        return facultyService.addFaculty(faculty);
    }

    @Operation(summary = "Get Faculty", description = "Get all Faculty")
    @GetMapping("/getAll")
    public ResponseEntity<HttpResponse> getAll(){ return facultyService.getAllFaculties();}

    @Operation(summary = "Get by ID", description = "Get single faculty by id")
    @GetMapping("/getById/{id}")
    public ResponseEntity<HttpResponse> getById(@PathVariable("id") Long id) throws NotFoundException {
        return facultyService.getFacultyById(id);
    }

    @Operation(summary = "Delete Faculty", description = "Delete single faculty by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) throws NotFoundException {
        return facultyService.deleteFacultyById(id);
    }

    @Operation(summary = "Update Faculty")
    @PutMapping("/update")
    public ResponseEntity<HttpResponse> update(@RequestBody Faculty faculty) throws AlreadyExistException {
        return facultyService.updateFaculty(faculty);
    }
}
