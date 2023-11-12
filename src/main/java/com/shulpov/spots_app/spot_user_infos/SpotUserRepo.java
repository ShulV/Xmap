package com.shulpov.spots_app.spot_user_infos;

import com.shulpov.spots_app.spot_user_infos.models.SpotUser;
import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface SpotUserRepo extends JpaRepository<SpotUser, Long> {

    Optional<SpotUser> findByPostedSpotAndUserActor(Spot spot, User user);

    //Подсчитать количество лайков у спота
    @Query("SELECT COUNT(ss) FROM SpotUser ss WHERE ss.postedSpot=:spot and ss.liked = true")
    Integer getCountLikes(@Param("spot") Spot spot);

    //Подсчитать количество добавлений в избранное у спота
    @Query("SELECT COUNT(ss) FROM SpotUser ss WHERE ss.postedSpot=:spot and ss.favorite = true")
    Integer getCountFavorites(@Param("spot") Spot spot);

    //получить сущности SpotUser, где liked = true и spot = spot
    @Query("SELECT ss FROM SpotUser ss WHERE ss.userActor=:user and ss.liked = true")
    List<SpotUser> findByUserWhereLikedTrue(User user);

    //получить сущности SpotUser, где favorite = true и spot = spot
    @Query("SELECT ss FROM SpotUser ss WHERE ss.userActor=:user and ss.favorite = true")
    List<SpotUser> findByUserWhereFavoriteTrue(User user);
}
