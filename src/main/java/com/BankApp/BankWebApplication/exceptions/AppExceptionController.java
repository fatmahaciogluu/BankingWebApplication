package com.BankApp.BankWebApplication.exceptions;

import org.hibernate.JDBCException;
import org.hibernate.resource.beans.container.internal.NoSuchBeanException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@Controller
@ControllerAdvice
public class AppExceptionController {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException nullPointerException) {
        return new ResponseEntity<String>("Null value is not allowed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        return new ResponseEntity<String>("Account holder not found!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        return new ResponseEntity<String>("Your inputs are invalid. Please look into it!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchBeanException.class)
    public ResponseEntity<String> handleNoSuchBeanException(NoSuchBeanException noSuchBeanException) {
        return new ResponseEntity<String>("Required bean doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleClientErrorException(HttpClientErrorException clientErrorException) {
        return new ResponseEntity<String>("Something is wrong at your hand. Please look into your request!", HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<String> handleForbiddenException() {
        return new ResponseEntity<String>("You are not authorized to access this resource", HttpStatus.FORBIDDEN);
    }*/

    @ExceptionHandler(JDBCException.class)
    public ResponseEntity<String> handleJDBCException() {
        return new ResponseEntity<String>("Please enter different username or email", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException() {
        return new ResponseEntity<String>("You don't have the sufficient funds to perform this transaction", HttpStatus.BAD_REQUEST);
    }
}
