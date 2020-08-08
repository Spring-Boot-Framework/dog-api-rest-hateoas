package com.grz55.restapitraining.adoption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DogAdoptionController {

    private final DogAdoptionRepository dogAdoptionRepository;
    private final DogAdoptionModelAssembler dogAdoptionModelAssembler;

    @Autowired
    DogAdoptionController(DogAdoptionRepository dogAdoptionRepository, DogAdoptionModelAssembler dogAdoptionModelAssembler) {
        this.dogAdoptionRepository = dogAdoptionRepository;
        this.dogAdoptionModelAssembler = dogAdoptionModelAssembler;
    }

    @GetMapping("/adoptions")
    CollectionModel<EntityModel<DogAdoption>> all() {

        List<EntityModel<DogAdoption>> orders = dogAdoptionRepository.findAll().stream() //
                .map(dogAdoptionModelAssembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(orders, //
                linkTo(methodOn(DogAdoptionController.class).all()).withSelfRel());
    }

    @GetMapping("/adoptions/{id}")
    EntityModel<DogAdoption> one(@PathVariable Long id) {

        DogAdoption order = dogAdoptionRepository.findById(id) //
                .orElseThrow(() -> new DogAdoptionNotFoundException(id));

        return dogAdoptionModelAssembler.toModel(order);
    }

    @PostMapping("/adoptions")
    ResponseEntity<EntityModel<DogAdoption>> newAdoption(@RequestBody DogAdoption dogAdoption) {

        dogAdoption.setDogName(dogAdoption.getDogName());
        dogAdoption.setDogAdoptionStatus(DogAdoptionStatus.SHELTERED);
        DogAdoption newDogAdoption = dogAdoptionRepository.save(dogAdoption);

        return ResponseEntity //
                .created(linkTo(methodOn(DogAdoptionController.class).one(newDogAdoption.getId())).toUri()) //
                .body(dogAdoptionModelAssembler.toModel(newDogAdoption));
    }

    //@PutMapping("/adoptions/{id}/complete")   //WORKED IN POSTMAN, DIDNT WORK IN WEB BROWSER
    @GetMapping("/adoptions/{id}/complete")
    ResponseEntity<?> complete(@PathVariable Long id) {

        DogAdoption dogAdoption = dogAdoptionRepository.findById(id) //
                .orElseThrow(() -> new DogAdoptionNotFoundException(id));

        if (dogAdoption.getDogAdoptionStatus() == DogAdoptionStatus.CONTACTING_FAMILY) {
            dogAdoption.setDogAdoptionStatus(DogAdoptionStatus.ADOPTED);
            return ResponseEntity.ok(dogAdoptionModelAssembler.toModel(dogAdoptionRepository.save(dogAdoption)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't complete the adoption when the dog is in the " + dogAdoption.getDogAdoptionStatus() + " status"));
    }

    //@DeleteMapping("/adoptions/{id}/cancel") //WORKED IN POSTMAN, DIDNT WORK IN WEB BROWSER
    @GetMapping("/adoptions/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable Long id) {

        DogAdoption dogAdoption = dogAdoptionRepository.findById(id) //
                .orElseThrow(() -> new DogAdoptionNotFoundException(id));

        if (dogAdoption.getDogAdoptionStatus() == DogAdoptionStatus.CONTACTING_FAMILY) {
            dogAdoption.setDogAdoptionStatus(DogAdoptionStatus.SHELTERED);
            return ResponseEntity.ok(dogAdoptionModelAssembler.toModel(dogAdoptionRepository.save(dogAdoption)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't cancel adoption that is in the " + dogAdoption.getDogAdoptionStatus() + " status"));
    }

    //@PutMapping("/adoptions/{id}/adopt") //WORKED IN POSTMAN, DIDNT WORK IN WEB BROWSER
    @GetMapping("/adoptions/{id}/adopt")
    ResponseEntity<?> adopt(@PathVariable Long id) {

        DogAdoption dogAdoption = dogAdoptionRepository.findById(id) //
                .orElseThrow(() -> new DogAdoptionNotFoundException(id));

        if (dogAdoption.getDogAdoptionStatus() == DogAdoptionStatus.SHELTERED) {
            dogAdoption.setDogAdoptionStatus(DogAdoptionStatus.CONTACTING_FAMILY);
            return ResponseEntity.ok(dogAdoptionModelAssembler.toModel(dogAdoptionRepository.save(dogAdoption)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't adopt the dog that is in the " + dogAdoption.getDogAdoptionStatus() + " status"));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        dogAdoptionRepository.save(new DogAdoption("Ares",DogAdoptionStatus.SHELTERED));
        dogAdoptionRepository.save(new DogAdoption("Walker",DogAdoptionStatus.CONTACTING_FAMILY));
        dogAdoptionRepository.save(new DogAdoption("Barney",DogAdoptionStatus.ADOPTED));
    }


}
