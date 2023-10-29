package com.shulpov.spots_app.location.repo;

import com.shulpov.spots_app.location.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {

}