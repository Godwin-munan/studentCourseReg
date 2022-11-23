package com.munan.studentCourseReg.constants;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URI_Constant {

    public static URI getURL(String stringPath) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(stringPath).toString());
    }
}
