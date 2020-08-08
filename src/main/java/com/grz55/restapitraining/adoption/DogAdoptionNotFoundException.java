package com.grz55.restapitraining.adoption;

public class DogAdoptionNotFoundException extends RuntimeException {

    public DogAdoptionNotFoundException(Long id) {
        super("Dog adoption id = " + id + " not found");
    }
}
