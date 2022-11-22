package com.munan.studentCourseReg.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.munan.studentCourseReg.constants.SecurityConstant.EXPIRATION;
import static com.munan.studentCourseReg.constants.SecurityConstant.KEY;

@Service
public class JwtUtil {


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
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

    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
        final String username;

        try{
            username = extractUsername(token);
        }catch(BadCredentialsException | MalformedJwtException e){
            throw new BadCredentialsException("Invalid credential");
        }catch(ExpiredJwtException e){
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "Token has expired", e);
        }

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
