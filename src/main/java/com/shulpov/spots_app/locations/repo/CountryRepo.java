package com.shulpov.spots_app.locations.repo;

import com.shulpov.spots_app.locations.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {

}