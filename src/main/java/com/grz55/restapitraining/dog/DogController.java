package com.grz55.restapitraining.dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DogController {

    private DogRepository dogRepository;
    private final DogModelAssembler dogModelAssembler;

    @Autowired
    public DogController(DogRepository dogRepository, DogModelAssembler dogModelAssembler) {
        this.dogRepository = dogRepository;
        this.dogModelAssembler = dogModelAssembler;
    }

    @GetMapping("/dogs")
    CollectionModel<EntityModel<Dog>> all() {

        List<EntityModel<Dog>> dogs = dogRepository.findAll().stream() //
                .map(dogModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @PostMapping("/dogs")
    ResponseEntity<?> newDog(@RequestBody Dog newEmployee) {

        EntityModel<Dog> entityModel = dogModelAssembler.toModel(dogRepository.save(newEmployee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/dogs/{id}")
    EntityModel<Dog> one(@PathVariable Long id) {

        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        return dogModelAssembler.toModel(dog);
    }

    @PutMapping("/dogs/{id}")
    ResponseEntity<?> replaceDog(@RequestBody Dog newDog, @PathVariable Long id) {

        Dog updatedDog = dogRepository.findById(id) //
                .map(dog -> {
                    dog.setName(newDog.getName());
                    dog.setBreed(newDog.getBreed());
                    return dogRepository.save(dog);
                }) //
                .orElseGet(() -> {
                    newDog.setId(id);
                    return dogRepository.save(newDog);
                });

        EntityModel<Dog> entityModel = dogModelAssembler.toModel(updatedDog);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/dogs/{id}")
    ResponseEntity<?> deleteDog(@PathVariable Long id) {
        dogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        dogRepository.save(new Dog("Ares", "German Shepherd"));
        dogRepository.save(new Dog("Walker", "Golden Retriever"));
        dogRepository.save(new Dog("Barney", "Rottweiler"));
    }
}
