package com.grz55.restapitraining.adoption;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DogAdoptionModelAssembler implements RepresentationModelAssembler<DogAdoption, EntityModel<DogAdoption>> {

    @Override
    public EntityModel<DogAdoption> toModel(DogAdoption dogAdoption) {
        EntityModel<DogAdoption> dogAdoptionModel = EntityModel.of(dogAdoption,
                linkTo(methodOn(DogAdoptionController.class).one(dogAdoption.getId())).withSelfRel(),
                linkTo(methodOn(DogAdoptionController.class).all()).withRel("adoptions"));

        if (dogAdoption.getDogAdoptionStatus() == DogAdoptionStatus.CONTACTING_FAMILY) {
            dogAdoptionModel.add(linkTo(methodOn(DogAdoptionController.class).cancel(dogAdoption.getId())).withRel("cancel"));
            dogAdoptionModel.add(linkTo(methodOn(DogAdoptionController.class).complete(dogAdoption.getId())).withRel("complete"));
        }

        if (dogAdoption.getDogAdoptionStatus() == DogAdoptionStatus.SHELTERED) {
            dogAdoptionModel.add(linkTo(methodOn(DogAdoptionController.class).adopt(dogAdoption.getId())).withRel("adopt"));
        }

        return dogAdoptionModel;
    }
}
