package com.munan.studentCourseReg.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Function;
import static com.munan.studentCourseReg.constants.SecurityConstant.EXPIRATION;
import static com.munan.studentCourseReg.constants.SecurityConstant.KEY;

@Service
public class JwtUtil {


    public List<SimpleGrantedAuthority> extractRole(String token){
        List<SimpleGrantedAuthority> roles = null;

        Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isUser = claims.get("isUser", Boolean.class);
        if(isAdmin != null && isAdmin){

            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if(isUser != null && isUser){

            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return roles;
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractIssuer(String token){
        return extractClaim(token, Claims::getIssuer);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            claims.put("isAdmin", true);
        }

        if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))){
            claims.put("isUser", true);
        }
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();

    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token){

        final String username = extractUsername(token);
        final Boolean inValid = isTokenExpired(token);



        return (username != null && !inValid);
    }
}
