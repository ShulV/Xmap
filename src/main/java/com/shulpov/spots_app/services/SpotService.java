package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.repo.SpotRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SpotService {

    private final SpotRepo spotRepo;
    private final ImageInfoService imageInfoService;

//    private final EntityManager entityManager;

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
        return spotRepo.findById(spotId);
    }

    public Spot saveWithAvatars(MultipartFile[] files, Spot spot) throws IOException {
        Spot newSpot = spotRepo.save(spot);
        Long spotId = newSpot.getId();
        for(int i=0; i<files.length; i++) {
            imageInfoService.uploadSpotImage(files[i], spotId);
        }
//        entityManager.refresh(newSpot);
        return newSpot;
    }
}
