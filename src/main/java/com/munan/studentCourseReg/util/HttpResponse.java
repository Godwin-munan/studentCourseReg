package com.munan.studentCourseReg.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HttpResponse<T> {

    @JsonProperty("response")
    private String response;

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private int status;

    private HttpStatus httpStatus;
    @JsonProperty("data")
    private T data;

    public HttpResponse() {
    }

    public HttpResponse(int status, String response, String message) {
        this.status = status;
        this.response = response;
        this.message = message;
    }

    public HttpResponse(int status, String response, T data) {
        this.status = status;
        this.response = response;
        this.data = data;
    }

    public HttpResponse(int status, HttpStatus httpStatus, String response, String message) {
        this.status = status;
        this.httpStatus=httpStatus;
        this.message = message;
        this.response=response;
    }
}
