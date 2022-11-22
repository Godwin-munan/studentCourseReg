package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.model.AppUser;
import com.munan.studentCourseReg.model.Role;
import com.munan.studentCourseReg.service.AppUserService;
import com.munan.studentCourseReg.util.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "User Controller")
public class UserController {

    private final AppUserService userService;

    @Operation(summary = "Add Role", description = "Add new Role")
    @PostMapping("/add/role")
    public ResponseEntity<HttpResponse> addRole(@RequestBody Role role) throws AlreadyExistException {
        return userService.addRole(role);
    }

    @Operation(summary = "Register User", description = "Register new User")
    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody AppUser user) throws AlreadyExistException {
        return userService.register(user);
    }

}
