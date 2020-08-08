package com.grz55.restapitraining.adoption;

import javax.persistence.*;

@Entity
@Table(name = "DOG_ADOPTION")
public class DogAdoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String dogName;

    private DogAdoptionStatus dogAdoptionStatus;

    public DogAdoption() {
    }

    public DogAdoption(String dogName, DogAdoptionStatus dogAdoptionStatus) {
        this.dogName = dogName;
        this.dogAdoptionStatus = dogAdoptionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public DogAdoptionStatus getDogAdoptionStatus() {
        return dogAdoptionStatus;
    }

    public void setDogAdoptionStatus(DogAdoptionStatus dogAdoptionStatus) {
        this.dogAdoptionStatus = dogAdoptionStatus;
    }
}
