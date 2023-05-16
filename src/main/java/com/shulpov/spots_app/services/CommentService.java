package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.Comment;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.CommentRepo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class CommentService {
    private final CommentRepo commentRepo;
    private final SpotService spotService;

    public CommentService(CommentRepo commentRepo, SpotService spotService) {
        this.commentRepo = commentRepo;
        this.spotService = spotService;
    }

    //Получить комментарии спота по id спота
    public List<Comment> findByCommentedSpotId(Long spotId) throws NoSuchElementException{
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isPresent()) {
            List<Comment> comments = commentRepo.findByCommentedSpot(spotOpt.get());
            return comments;
        } else {
            throw new NoSuchElementException("Нет спота с id = " + spotId);
        }
    }

    //Сохранить спот
    public Comment save(Comment comment, User user, Long spotId) {
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isPresent()) {
            comment.setUploadDate(new Date(System.currentTimeMillis()));
            comment.setCommentedSpot(spotOpt.get());
            comment.setCommentator(user);
            return commentRepo.save(comment);
        } else {
            throw new NoSuchElementException("Spot with id = " + spotId + " doesn't exist");
        }
    }
}
