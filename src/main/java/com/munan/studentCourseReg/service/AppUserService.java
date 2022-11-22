package com.munan.studentCourseReg.service;

import com.munan.studentCourseReg.dto.AuthRequestDto;
import com.munan.studentCourseReg.dto.AuthResponseDto;
import com.munan.studentCourseReg.exception.AlreadyExistException;
import com.munan.studentCourseReg.exception.NotFoundException;
import com.munan.studentCourseReg.model.AppUser;
import com.munan.studentCourseReg.model.Role;
import com.munan.studentCourseReg.repository.AppUserRepository;
import com.munan.studentCourseReg.repository.RoleRepository;
import com.munan.studentCourseReg.security.MyUserDetailService;
import com.munan.studentCourseReg.util.HttpResponse;
import com.munan.studentCourseReg.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailService userDetailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private URI getURL(String stringPath) {

        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(stringPath).toString());
    }

    //COMMAND LINE METHOD
    public void commandLine(Role role, AppUser user) throws AlreadyExistException {
        Optional<Role> findRole = roleRepository.findByName(role.getName());

        if(findRole.isPresent()){
            throw new AlreadyExistException(role.getName()+" already exist");
        }
        roleRepository.save(role);

        Optional<AppUser> findUser = userRepository.findByUsername(user.getUsername());

        if(findUser.isPresent()){
            throw new AlreadyExistException(user.getUsername()+" User already exist");
        }


        Set<Role> roles = user.getRoles().stream().map(
                _role -> roleRepository.findByName(_role.getName()).get()).collect(Collectors.toSet());



        AppUser newUser = new AppUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(roles);

        userRepository.save(newUser);

    }

    //ADD NEW ROLE
    public ResponseEntity<HttpResponse> addRole(Role role) throws AlreadyExistException {

        Optional<Role> findRole = roleRepository.findByName(role.getName());

        if(findRole.isPresent()){
            throw new AlreadyExistException(role.getName()+" already exist");
        }

        Role newRole = roleRepository.save(role);
        URI uri = getURL("/api/user/add/role");

       return ResponseEntity.created(uri).body(new HttpResponse<>(HttpStatus.OK.value(), "Successful", newRole));
    }


    //REGISTER NEW USER
    public ResponseEntity<HttpResponse> register(AppUser user) throws AlreadyExistException {
        Optional<AppUser> findUser = userRepository.findByUsername(user.getUsername());

        if(findUser.isPresent()){
            throw new AlreadyExistException(user.getUsername()+" User already exist");
        }


        Set<Role> roles = user.getRoles().stream().map(
                role -> roleRepository.findByName(role.getName()).get()).collect(Collectors.toSet());


        AppUser newUser = new AppUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(roles);

        URI uri = getURL("/api/authenticate/register");

        return ResponseEntity.created(uri)
                .body(new HttpResponse<>(HttpStatus.OK.value(), "Successful", userRepository.save(newUser)));
    }


    public ResponseEntity<HttpResponse<?>> createAuthToken(AuthRequestDto requestDto) throws NotFoundException {

        try{
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(), requestDto.getPassword()
                    );
            authenticationManager.authenticate(token);
        }catch (Exception e){
            throw new NotFoundException(e.getMessage());
        }

        final UserDetails userDetails = userDetailService
                .loadUserByUsername(requestDto.getEmail());

        String jwt = jwtUtil.generateToken(userDetails);

        AuthResponseDto responseDto = new AuthResponseDto(jwt, "JWT token");


        return ResponseEntity.ok(
                new HttpResponse<>(HttpStatus.OK.value(), "Successful", responseDto)
        );
    }
}
