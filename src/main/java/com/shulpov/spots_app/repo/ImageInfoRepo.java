package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.ImageInfo;
import com.shulpov.spots_app.models.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageInfoRepo extends JpaRepository<ImageInfo, Long> {

    @Modifying
    @Query(nativeQuery = true, value="DELETE FROM image_info where id=:id")
    void deleteByIdWithoutRefs(@Param("id") Long id);
}
