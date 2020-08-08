package com.grz55.restapitraining.adoption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogAdoptionRepository extends JpaRepository<DogAdoption, Long> {
}
