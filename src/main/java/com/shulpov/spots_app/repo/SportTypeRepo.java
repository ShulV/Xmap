package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportTypeRepo extends JpaRepository<SportType, Integer> {
}
