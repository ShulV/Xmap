package com.shulpov.spots_app.repo;

import com.shulpov.spots_app.models.Comment;
import com.shulpov.spots_app.models.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentedSpot(Spot spot);
}
