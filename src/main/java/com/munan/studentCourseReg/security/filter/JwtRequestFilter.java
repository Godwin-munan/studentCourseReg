package com.munan.studentCourseReg.security.filter;


import com.munan.studentCourseReg.security.JwtAuthEntryPoint;
import com.munan.studentCourseReg.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.munan.studentCourseReg.constants.URI_Constant.AUTH_URL;
import static com.munan.studentCourseReg.constants.URI_Constant.REFRESH_URL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final JwtAuthEntryPoint jwtAuthEntryPoint;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals(AUTH_URL) || request.getServletPath().equals(REFRESH_URL)){
            filterChain.doFilter(request, response);

        }else {

            final String authorizationHeader = request.getHeader(AUTHORIZATION);
            String username = null;
            String jwt = null;
            String issuer = null;


                try{

                    if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer")){
                        jwt = authorizationHeader.split(" ")[1].trim();
                        username = jwtUtil.extractUsername(jwt);
                        issuer = jwtUtil.extractIssuer(jwt);
                    }

                } catch (ExpiredJwtException e){
                    logger.error("Expired JWT");
                    response.setHeader("error", "Expired JWT");
                    jwtAuthEntryPoint.commence(request, response, new AuthenticationException("Expired JWT"){});
                }catch (BadCredentialsException e){
                    logger.error("Wrong Credentials");
                    response.setHeader("error", "Wrong Credentials");
                    jwtAuthEntryPoint.commence(request, response, new AuthenticationException("Bad Credentials on Jwt"){});
                }catch (MalformedJwtException e){
                    logger.error(e.getMessage());
                    response.setHeader("error", "Bad Token");
                    jwtAuthEntryPoint.commence(request, response, new AuthenticationException("Bad Token"){});
                }
                catch (Exception e  ){
                    logger.error(e.getMessage());
                    response.setHeader("error", e.getMessage());
                    jwtAuthEntryPoint.commence(request, response, new AuthenticationException(e.getMessage()){});
                }


                try{

                    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null && issuer == null){

                        boolean valid = jwtUtil.validateToken(jwt);
                        List<SimpleGrantedAuthority> roles = jwtUtil.extractRole(jwt);
                        if(valid){

                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            username,
                                            null,
                                            roles
                                    );
                            usernamePasswordAuthenticationToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                            logger.info("Authentication Successful");
                        }
                    }
                }catch (Exception e){
                    logger.error(e.getMessage());
                    jwtAuthEntryPoint.commence(request, response, new AuthenticationException(e.getMessage()){});
                }

            filterChain.doFilter(request, response);
        }

    }
}
