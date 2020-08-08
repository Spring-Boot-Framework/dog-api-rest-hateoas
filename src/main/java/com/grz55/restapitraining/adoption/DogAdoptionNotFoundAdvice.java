package com.grz55.restapitraining.adoption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DogAdoptionNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(DogAdoptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String dogAdoptionNotFoundHandler(DogAdoptionNotFoundException ex) {
        return ex.getMessage();
    }

}
