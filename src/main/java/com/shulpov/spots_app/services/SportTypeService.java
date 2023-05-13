package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.SportType;
import com.shulpov.spots_app.repo.SportTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportTypeService {
    private final SportTypeRepo sportTypeRepo;

    @Autowired
    public SportTypeService(SportTypeRepo sportTypeRepo) {
        this.sportTypeRepo = sportTypeRepo;
    }

    //Получить все типы спорта
    public List<SportType> getAll() {
        return sportTypeRepo.findAll();
    }
}
