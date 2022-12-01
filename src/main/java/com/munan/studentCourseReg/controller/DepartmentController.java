package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.DeptDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.GlobalExceptionHandling;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.service.DepartmentService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "Department Controller")
public class DepartmentController extends GlobalExceptionHandling {

    private final DepartmentService departmentService;

    @Operation(summary = "Add Department", description = "Add new Department")
    @PostMapping("/add")
    public ResponseEntity<HttpResponse<?>> add(@RequestBody DeptDto deptDto) throws AlreadyExistException, NotFoundException {
        return departmentService.addDept(deptDto);
    }

    @Operation(summary = "Get Department", description = "Get all Department")
    @GetMapping("/get/getAll/{page}/{size}/{field}")
    public ResponseEntity<HttpResponse<?>> getAll(@PathVariable("page") Integer page,
                                                  @PathVariable("size") Integer size,
                                                  @PathVariable("field") String field){
        return departmentService.getAllDepartments(page, size, field);
    }

    @Operation(summary = "Get by ID", description = "Get single department by id")
    @GetMapping("/get/getById/{id}")
    public ResponseEntity<HttpResponse<?>> getById(@PathVariable("id") Long id) throws NotFoundException {
        return departmentService.getDeptById(id);
    }

    @Operation(summary = "Delete Department", description = "Delete single department by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse<?>> delete(@PathVariable("id") Long id) throws NotFoundException, AlreadyExistException {
        return departmentService.deleteDeptById(id);
    }

    @Operation(summary = "Update Department", description = "Update department record")
    @PutMapping("/update")
    public ResponseEntity<HttpResponse<?>> update(@RequestBody DeptDto deptDto) throws AlreadyExistException {
        return departmentService.updateDept(deptDto);
    }
}
