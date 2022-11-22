package com.munan.studentCourseReg.constants;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecurityConstant {
    public static final String JWT_SECRET = "BreakMakeISeeMrMrsBreakerBreakMakeISeeMrMrsBreaker";

    public final static Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static  Integer EXPIRATION = 1000 * 60 * 60 * 10;
}
