package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.Country;
import com.shulpov.spots_app.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepo extends JpaRepository<Region, Integer> {
    List<Region> findByCountry(Country country);
}
