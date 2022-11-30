package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.AuthRequestDto;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Controls Login and Registration")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserService userService;


    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate ", description = "Authenticate to get JWT token")
    public void createAuthToken(@RequestBody AuthRequestDto requestDto) throws NotFoundException {
//        return userService.createAuthToken(requestDto);
    }

    @GetMapping("/refresh")
    @Operation(summary = "Refresh", description = "Refresh to get JWT token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws NotFoundException, IOException {
         userService.createAuthToken(request, response);
    }
}
