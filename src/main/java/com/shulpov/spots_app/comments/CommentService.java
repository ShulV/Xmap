package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.spots.models.Spot;
import com.shulpov.spots_app.spots.SpotService;
import com.shulpov.spots_app.users.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;

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

    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepo commentRepo, @Lazy SpotService spotService) {
        this.commentRepo = commentRepo;
        this.spotService = spotService;
    }

    //Получить комментарии спота по id спота
    public List<Comment> findByCommentedSpotId(Long spotId) throws NoSuchElementException{
        logger.atInfo().log("findByCommentedSpotId spotId={}", spotId);
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isPresent()) {
            List<Comment> comments = commentRepo.findByCommentedSpot(spotOpt.get());
            logger.atInfo().log("comments: size={}", comments.size());
            return comments;
        } else {
            logger.atInfo().log("No spot with id = {}", spotId);
            throw new NoSuchElementException("No spot with id = " + spotId);
        }
    }

    @Transactional(rollbackFor = NoTransactionException.class)
    //Сохранить комментарий
    public Comment save(Comment comment, User user, Long spotId) {
        logger.atInfo().log("save - comment: text={} username={} spotId={}",
                comment.getText(), user.getName(), spotId);
        Optional<Spot> spotOpt = spotService.findById(spotId);
        if(spotOpt.isPresent()) {
            comment.setUploadDate(new Date(System.currentTimeMillis()));
            comment.setCommentedSpot(spotOpt.get());
            comment.setCommentator(user);
            logger.atInfo().log("Spot with id = {} saving", spotId);
            return commentRepo.save(comment);
        } else {
            logger.atError().log("Spot with id = {} doesn't exist", spotId);
            throw new NoSuchElementException("Spot with id = " + spotId + " doesn't exist");
        }
    }

    @Transactional(rollbackFor = NoTransactionException.class)
    //Удалить комментарий
    public void deleteById(Long commentId) {
        logger.atInfo().log("deleteById id={}", commentId);
        commentRepo.deleteById(commentId);
    }
}
