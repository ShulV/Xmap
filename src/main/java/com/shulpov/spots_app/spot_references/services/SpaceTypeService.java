package com.shulpov.spots_app.spot_references.services;

import com.shulpov.spots_app.spot_references.models.SpaceType;
import com.shulpov.spots_app.spot_references.repo.SpaceTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class SpaceTypeService {
    private final SpaceTypeRepo spaceTypeRepo;

    private final Logger logger = LoggerFactory.getLogger(SpaceTypeService.class);

    @Autowired
    public SpaceTypeService(SpaceTypeRepo spaceTypeRepo) {
        this.spaceTypeRepo = spaceTypeRepo;
    }

    //Получить все типы помещений
    public List<SpaceType> getAll() {
        return spaceTypeRepo.findAll();
    }

    //Получить тип помещения по id
    public Optional<SpaceType> getById(Integer id) {
        return spaceTypeRepo.findById(id);
    }
}
