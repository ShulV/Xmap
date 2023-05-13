package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.SpaceType;
import com.shulpov.spots_app.models.SportType;
import com.shulpov.spots_app.repo.SpaceTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceTypeService {
    private final SpaceTypeRepo spaceTypeRepo;

    private final static Logger logger = LoggerFactory.getLogger(SpaceTypeService.class);

    @Autowired
    public SpaceTypeService(SpaceTypeRepo spaceTypeRepo) {
        this.spaceTypeRepo = spaceTypeRepo;
    }

    //Получить все типы помещений
    public List<SpaceType> getAll() {
        return spaceTypeRepo.findAll();
    }
}
