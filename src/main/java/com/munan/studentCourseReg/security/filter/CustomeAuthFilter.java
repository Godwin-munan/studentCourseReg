package com.munan.studentCourseReg.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.munan.studentCourseReg.security.JwtAuthEntryPoint;
import com.munan.studentCourseReg.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.munan.studentCourseReg.constants.SecurityConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomeAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService  userDetailService;

    private  final JwtUtil jwtUtil;

    private String username;
    private  String password;

    private final ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken token = null;
        Authentication authentication = null;


        try {
            byte[] inputStreamBytes = StreamUtils.copyToByteArray(request.getInputStream());
            Map<String, String> jsonRequest = mapper.readValue(inputStreamBytes, Map.class);

            username = jsonRequest.get("email");
            password = jsonRequest.get("password");

            token = new UsernamePasswordAuthenticationToken(username, password);
             authentication = authenticationManager.authenticate(token);

        } catch (Exception e) {
            logger.error(e.getMessage());
            jwtAuthEntryPoint.commence(request, response, new AuthenticationException(e.getMessage()){});
        }
        return authentication;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        final UserDetails userDetails = userDetailService
                .loadUserByUsername(username);

        String access_token = jwtUtil.generateToken(userDetails);

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "/api/auth/authenticate");

        String refresh_token = Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + RE_EXPIRATION))
                .signWith(KEY)
                .compact();

        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);
        token.put("refresh_token", refresh_token);



        response.setContentType(APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getOutputStream(), token);

    }
}


