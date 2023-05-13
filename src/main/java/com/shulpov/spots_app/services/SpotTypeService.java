package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.SpotType;
import com.shulpov.spots_app.repo.SpotTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotTypeService {
    private final SpotTypeRepo spotTypeRepo;

    @Autowired
    public SpotTypeService(SpotTypeRepo spotTypeRepo) {
        this.spotTypeRepo = spotTypeRepo;
    }

    //Получить все типы спотов
    public List<SpotType> getAll() {
        return spotTypeRepo.findAll();
    }
}
