package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.spots.models.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentedSpot(Spot spot);
}
