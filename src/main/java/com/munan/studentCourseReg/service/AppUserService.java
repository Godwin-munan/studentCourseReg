package com.munan.studentCourseReg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import static com.munan.studentCourseReg.constants.URI_Constant.getURL;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    //COMMAND LINE METHOD
    public void commandLine(Role role, AppUser user) throws AlreadyExistException {
        Optional<Role> findRole = roleRepository.findByName(role.getName());

        if(findRole.isPresent()){
            throw new AlreadyExistException(role.getName()+" already exist");
        }
        roleRepository.save(role);

        AppUser newUser = findUser(user);

        userRepository.save(newUser);

    }

    //ADD NEW ROLE
    public ResponseEntity<HttpResponse<?>> addRole(Role role) throws AlreadyExistException {

        Optional<Role> findRole = roleRepository.findByName(role.getName());

        if(findRole.isPresent()){
            throw new AlreadyExistException(role.getName()+" already exist");
        }

        Role newRole = roleRepository.save(role);
        URI uri = getURL("/api/user/add/role");

       return ResponseEntity.created(uri).body(new HttpResponse<>(HttpStatus.OK.value(), "Successful", newRole));
    }


    //REGISTER NEW USER
    public ResponseEntity<HttpResponse<?>> register(AppUser user) throws AlreadyExistException {

        AppUser newUser = findUser(user);

        URI uri = getURL("/api/authenticate/register");

        return ResponseEntity.created(uri)
                .body(new HttpResponse<>(HttpStatus.CREATED.value(), "Successful", userRepository.save(newUser)));
    }

    private AppUser findUser(AppUser user) throws AlreadyExistException {
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

        return newUser;
    }


    //CREATE NEW TOKEN USING REFRESH TOKEN
    public void createAuthToken(HttpServletRequest request, HttpServletResponse response) throws NotFoundException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> error = new HashMap<>();
        String jwtToken = null;
        String username = null;
        String issuer = null;

        response.setContentType(APPLICATION_JSON_VALUE);

        try{
            if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer")){

                jwtToken = authorizationHeader.split(" ")[1].trim();
                username = jwtUtil.extractUsername(jwtToken);
                issuer = jwtUtil.extractIssuer(jwtToken);
            }else {
                throw new Exception("Invalid Token");
            }
        }catch (Exception e){
            response.setStatus(SC_BAD_REQUEST);
            error.put("error",e.getMessage());
            System.out.println(e.getMessage());
            mapper.writeValue(response.getOutputStream(), error);
        }


        if(username != null && Objects.equals(issuer, "/api/auth/authenticate")){

            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            try{
                if(jwtUtil.validateToken(jwtToken)){
                    String access_token = jwtUtil.generateToken(userDetails);

                    response.setHeader("access_token", access_token);

                    Map<String, String> token = new HashMap<>();
                    token.put("access_token", access_token);



                    response.setContentType(APPLICATION_JSON_VALUE);
                    mapper.writeValue(response.getOutputStream(), token);
                }
            }catch (Exception e){
                response.setStatus(SC_BAD_REQUEST);
                response.setHeader("error", e.getMessage());
                error.put("error",e.getMessage());
                System.out.println(e.getMessage());
                mapper.writeValue(response.getOutputStream(), error);
            }
        }


    }

}

