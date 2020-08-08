package com.grz55.restapitraining.dog;

public class DogNotFoundException extends RuntimeException {

    public DogNotFoundException(Long id) {
        super("Dog id = " + id + " not found");
    }
}
