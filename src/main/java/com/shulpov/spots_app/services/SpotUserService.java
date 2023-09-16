package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.SpotUser;
import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.models.pk.UserSpotPK;
import com.shulpov.spots_app.repo.SpotUserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class SpotUserService {
    private final SpotUserRepo spotUserRepo;
    private final Logger logger = LoggerFactory.getLogger(SpotUserService.class);
    @Autowired
    public SpotUserService(SpotUserRepo spotUserRepo) {
        this.spotUserRepo = spotUserRepo;
    }

    //Изменить состояние лайка (убрать, если поставлен, добавить, если не поставлен)
    @Transactional
    public Map<String, Object> changeLikeState(Spot spot, User user) {
        logger.atInfo().log("changeLikeState spot_id={} user_id={}", spot.getId(), user.getId());
        Optional<SpotUser> spotUserOpt = spotUserRepo.findByPostedSpotAndUserActor(spot, user);
        if(spotUserOpt.isPresent()) {
            logger.atInfo().log("changeLikeState spot_id={} user_id={} change old instance", spot.getId(), user.getId());
            SpotUser spotUser = spotUserOpt.get();
            spotUser.setLiked(!spotUser.getLiked());
            SpotUser newSpotUser = spotUserRepo.save(spotUser);
            return Map.of("spotId", newSpotUser.getPostedSpot().getId(),
                    "userId", newSpotUser.getUserActor().getId());
        } else {
            logger.atInfo().log("changeLikeState spot_id={} user_id={} create new instance", spot.getId(), user.getId());
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
        logger.atInfo().log("changeFavoriteState spot_id={} user_id={}", spot.getId(), user.getId());
        Optional<SpotUser> spotUserOpt = spotUserRepo.findByPostedSpotAndUserActor(spot, user);
        if(spotUserOpt.isPresent()) {
            logger.atInfo().log("changeFavoriteState spot_id={} user_id={} change old instance", spot.getId(), user.getId());
            SpotUser spotUser = spotUserOpt.get();
            spotUser.setFavorite(!spotUser.getFavorite());
            SpotUser newSpotUser = spotUserRepo.save(spotUser);
            return Map.of("spotId", newSpotUser.getPostedSpot().getId(),
                    "userId", newSpotUser.getUserActor().getId());
        } else {
            logger.atInfo().log("changeFavoriteState spot_id={} user_id={} create new instance", spot.getId(), user.getId());
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

    //получить количество лайков у спота
    public Integer getLikeNumber(Spot spot) {
        Integer likeNum = spotUserRepo.getCountLikes(spot);
        logger.atInfo().log("getLikeNumber spot_id={} likes={}", spot.getId(), likeNum);
        return likeNum;
    }

    //получить количество добавлений в избранное у спота
    public Integer getFavoriteNumber(Spot spot) {
        Integer favoriteNum = spotUserRepo.getCountFavorites(spot);
        logger.atInfo().log("getFavoriteNumber spot_id={}, favorites={}", spot.getId(), favoriteNum);
        return favoriteNum;
    }

    //получить сущности SpotUser, где liked = true и spot = spot
    public List<SpotUser> getLikedSpotUsers(User user) {
        logger.atInfo().log("getLikedSpotUsers user_id={}", user.getId());
        return spotUserRepo.findByUserWhereLikedTrue(user);
    }

    //получить сущности SpotUser, где favorite = true и spot = spot
    public List<SpotUser> getFavoriteSpotUsers(User user) {
        logger.atInfo().log("getFavoriteSpotUsers user_id={}", user.getId());
        return spotUserRepo.findByUserWhereFavoriteTrue(user);
    }

    //получить сущность SpotUser
    public SpotUser getInfo(Spot spot, User user) {
        logger.atInfo().log("getInfo spot_id={} user_id={}", spot.getId(), user.getId());
        Optional<SpotUser> spotUserOpt = spotUserRepo.findByPostedSpotAndUserActor(spot, user);
        if (spotUserOpt.isEmpty()) {
            SpotUser spotUser = new SpotUser();
            spotUser.setUserActor(user);
            spotUser.setPostedSpot(spot);
            spotUser.setFavorite(false);
            spotUser.setLiked(false);
            return spotUser;
        }
        return spotUserOpt.get();
    }
}
