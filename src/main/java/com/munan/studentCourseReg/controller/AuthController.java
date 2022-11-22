package com.munan.studentCourseReg.controller;

import com.munan.studentCourseReg.dto.AuthRequestDto;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.security.MyUserDetailService;
import com.munan.studentCourseReg.service.AppUserService;
import com.munan.studentCourseReg.util.HttpResponse;
import com.munan.studentCourseReg.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Controls Login and Registration")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserService userService;


    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate ", description = "Authenticate to get JWT token")
    public ResponseEntity<HttpResponse<?>> createAuthToken(@RequestBody AuthRequestDto requestDto) throws NotFoundException {
        return userService.createAuthToken(requestDto);
    }
}
