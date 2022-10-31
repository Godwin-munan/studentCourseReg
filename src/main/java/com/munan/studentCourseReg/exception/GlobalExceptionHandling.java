package com.munan.studentCourseReg.exception;

import com.munan.studentCourseReg.util.HttpResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandling implements ErrorController {
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<HttpResponse> alreadyExistException(AlreadyExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> notFoundException(NotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

   /*


    @ExceptionHandler(UnderAgeException.class)
    public ResponseEntity<HttpResponse> underAgeException(UnderAgeException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }




    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<HttpResponse> notAllowedException(NotAllowedException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
*/

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }

}
