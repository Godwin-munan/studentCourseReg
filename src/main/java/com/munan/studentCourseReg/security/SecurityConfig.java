package com.munan.studentCourseReg.security;

import com.munan.studentCourseReg.security.filter.CustomeAuthFilter;
import com.munan.studentCourseReg.security.filter.JwtRequestFilter;
import com.munan.studentCourseReg.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.munan.studentCourseReg.constants.URI_Constant.AUTH_URL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final MyUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Bean
    public AuthenticationManager authManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomeAuthFilter customeAuthFilter = new CustomeAuthFilter(
                jwtAuthEntryPoint,
                authManager(),
                userDetailService,
                jwtUtil
        );

        customeAuthFilter.setFilterProcessesUrl(AUTH_URL);

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.
                        authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS))
                .addFilter(customeAuthFilter)
                        .authorizeHttpRequests(auth -> auth
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
                                .anyRequest().authenticated())
                .userDetailsService(userDetailService);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://**"));
        configuration.setAllowedMethods(List.of("GET", "POST","DELETE","PUT"));
        configuration.setAllowedHeaders(List.of(AUTHORIZATION));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }

}
