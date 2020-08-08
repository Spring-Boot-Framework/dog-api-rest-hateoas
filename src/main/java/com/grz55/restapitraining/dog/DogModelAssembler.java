package com.grz55.restapitraining.dog;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DogModelAssembler implements RepresentationModelAssembler<Dog, EntityModel<Dog>> {

    @Override
    public EntityModel<Dog> toModel(Dog dog) {

        return EntityModel.of(dog, //
                linkTo(methodOn(DogController.class).one(dog.getId())).withSelfRel(),
                linkTo(methodOn(DogController.class).all()).withRel("dogs"));
    }
}
