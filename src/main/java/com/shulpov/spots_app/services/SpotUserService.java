package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.SpotUser;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.models.pk.UserSpotPK;
import com.shulpov.spots_app.repo.SpotUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class SpotUserService {
    private final SpotUserRepo spotUserRepo;

    @Autowired
    public SpotUserService(SpotUserRepo spotUserRepo) {
        this.spotUserRepo = spotUserRepo;
    }

    //Изменить состояние лайка (убрать, если поставлен, добавить, если не поставлен)
    @Transactional
    public Map<String, Object> changeLikeState(Spot spot, User user) {
        Optional<SpotUser> spotUserOpt = spotUserRepo.findByPostedSpotAndUserActor(spot, user);
        if(spotUserOpt.isPresent()) {
            SpotUser spotUser = spotUserOpt.get();
            spotUser.setLiked(!spotUser.getLiked());
            SpotUser newSpotUser = spotUserRepo.save(spotUser);
            return Map.of("spotId", newSpotUser.getPostedSpot().getId(),
                    "userId", newSpotUser.getUserActor().getId());
        } else {
            SpotUser spotUser = new SpotUser();
            spotUser.setId(new UserSpotPK(spot.getId(), user.getId()));
            spotUser.setPostedSpot(spot);
            spotUser.setUserActor(user);
            spotUser.setLiked(true);
            spotUser.setFavorite(false);
            SpotUser newSpotUser = spotUserRepo.save(spotUser);
            return Map.of("spotId", newSpotUser.getPostedSpot().getId(),
                    "userId", newSpotUser.getUserActor().getId());
        }
    }

    //Изменить состояние наличия добавления спота в избранные (убрать, если есть, добавить, если нет)
    @Transactional
    public Map<String, Object> changeFavoriteState(Spot spot, User user) {
        Optional<SpotUser> spotUserOpt = spotUserRepo.findByPostedSpotAndUserActor(spot, user);
        if(spotUserOpt.isPresent()) {
            SpotUser spotUser = spotUserOpt.get();
            spotUser.setFavorite(!spotUser.getFavorite());
            SpotUser newSpotUser = spotUserRepo.save(spotUser);
            return Map.of("spotId", newSpotUser.getPostedSpot().getId(),
                    "userId", newSpotUser.getUserActor().getId());
        } else {
            SpotUser spotUser = new SpotUser();
            spotUser.setId(new UserSpotPK(spot.getId(), user.getId()));
            spotUser.setPostedSpot(spot);
            spotUser.setUserActor(user);
            spotUser.setFavorite(true);
            spotUser.setLiked(false);
            SpotUser newSpotUser = spotUserRepo.save(spotUser);
            return Map.of("spotId", newSpotUser.getPostedSpot().getId(),
                    "userId", newSpotUser.getUserActor().getId());
        }
    }
}
