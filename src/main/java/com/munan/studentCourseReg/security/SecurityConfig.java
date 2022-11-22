package com.munan.studentCourseReg.security;

import com.munan.studentCourseReg.security.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {

        return auth.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(POST,"/api/student/add/newStudent/**").permitAll()
                .antMatchers(
                        POST,
                        "/api/level/add/**",
                        "/api/gender/add/**",
                        "/api/faculty/add/**",
                        "/api/student/add/**",
                        "/api/course/add/**",
                        "/api/department/add/**")
                .hasRole("ADMIN")
                .antMatchers(
                        DELETE,
                        "/api/level/delete/**",
                        "/api/gender/delete/**",
                        "/api/faculty/delete/**",
                        "/api/student/delete/**",
                        "/api/course/delete/**",
                        "/api/department/delete/**")
                .hasRole("ADMIN")
                .antMatchers(
                        PUT,
                        "/api/level/update/**",
                        "/api/gender/update/**",
                        "/api/faculty/update/**",
                        "/api/student/update/**",
                        "/api/course/update/**",
                        "/api/department/update/**")
                .hasRole("ADMIN")
                .antMatchers("/api/get/**", "/add/student/{student_id}/courses").hasAnyRole("ADMIN", "USER")
                .antMatchers("/**","/auth").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}
