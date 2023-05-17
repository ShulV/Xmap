package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.SpotUser;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.models.pk.UserSpotPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotUserRepo extends JpaRepository<SpotUser, Long> {

//    @Query("from SpotUser as su where su.postedSpot = :spot and su.userActor = :user")
    Optional<SpotUser> findByPostedSpotAndUserActor(Spot spot, User user);
}
