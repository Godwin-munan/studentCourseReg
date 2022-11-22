package com.munan.studentCourseReg.controller;


import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.Level;
import com.munan.studentCourseReg.service.LevelService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/level")
@RequiredArgsConstructor
@Tag(name = "Level Controller", description = "Level Controller")
public class LevelController {

    private final LevelService levelService;


    @Operation(summary = "Add Level", description = "Add a new Level")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse<?>> add(@RequestBody Level level) throws AlreadyExistException {

        return levelService.addLevel(level);
    }

    @Operation(summary = "Get all levels", description = "Get all existing levels")
    @GetMapping("/get/getAll")
    public ResponseEntity<HttpResponse<?>> getAll(){ return levelService.getAllLevels();}

    @Operation(summary = "Delete level", description = "Delete existing level by id")
    @DeleteMapping("/delete/deleteById/{id}")
    public ResponseEntity<HttpResponse<?>> deleteById(@PathVariable("id") Long id) throws NotFoundException, AlreadyExistException { return levelService.deleteLevelById(id);}

    @Operation(summary = "Update level", description = "Update existing level")
    @PutMapping("/update")
    public ResponseEntity<HttpResponse<?>> updateGender(@RequestBody Level level) {
        return levelService.updateLevel(level);
    }
}
