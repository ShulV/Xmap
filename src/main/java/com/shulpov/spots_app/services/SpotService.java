package com.shulpov.spots_app.services;

import com.shulpov.spots_app.dto.SpotDto;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.repo.SpotRepo;
import com.shulpov.spots_app.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = "prototype")
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepo spotRepo;
    private final ImageInfoService imageInfoService;
    private final Logger logger = LoggerFactory.getLogger(SpotService.class);

    @Autowired
    public SpotService(SpotRepo spotRepo, @Lazy ImageInfoService imageInfoService) {
        this.spotRepo = spotRepo;
        this.imageInfoService = imageInfoService;
    }

    //Получить все споты
    public List<Spot> getAllSpots() {
        List<Spot> spots = spotRepo.findAll();
        logger.atInfo().log("getAllSpots() size={}", spots.size());
        return spots;
    }

    //Получить спот по id
    public Optional<Spot> findById(Long spotId) {
        logger.atInfo().log("findById id={}", spotId);
        return spotRepo.findById(spotId);
    }

    @Transactional
    public Spot saveWithAvatars(MultipartFile[] files, Spot spot) throws IOException {
        logger.atInfo().log("saveWithAvatars files.length={}, spot.name={}", files.length, spot.getName());
        spot = spotRepo.save(spot);
        Long spotId = spot.getId();
        for (MultipartFile file : files) {
            imageInfoService.uploadSpotImage(file, spotId);
        }
        return spot;
    }

    //Получить все споты в определенном радиусе
    public List<Spot> getSpotsInRadius(Double lat, Double lon, Double radius) {
        logger.atInfo().log("getSpotsInRadius lat={}, lon={}, rad={}", lat, lon, radius);
        return spotRepo.findSpotsInRadius(lat, lon, radius);
    }
}
