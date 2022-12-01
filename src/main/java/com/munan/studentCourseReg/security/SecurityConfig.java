package com.munan.studentCourseReg.security;

import com.munan.studentCourseReg.security.filter.CustomeAuthFilter;
import com.munan.studentCourseReg.security.filter.JwtRequestFilter;
import com.munan.studentCourseReg.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.munan.studentCourseReg.constants.URI_Constant.AUTH_URL;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final MyUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomeAuthFilter customeAuthFilter = new CustomeAuthFilter(
                jwtAuthEntryPoint,
          authenticationManager(),
          userDetailService,
          jwtUtil
        );

        customeAuthFilter.setFilterProcessesUrl(AUTH_URL);

        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .addFilter(customeAuthFilter)
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
    }
}
