package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.CommentDto;
import com.shulpov.spots_app.models.Comment;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.CommentService;
import com.shulpov.spots_app.services.UserService;
import com.shulpov.spots_app.utils.DtoConverter;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    private final DtoConverter dtoConverter;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, DtoConverter dtoConverter) {
        this.commentService = commentService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    @Transactional
    @PostMapping("/add-comment-by-spot-id/{spotId}")
    public Map<String, Object> getBySpotId(@PathVariable Long spotId,
                                           @RequestBody @Valid Comment comment,
                                           BindingResult bindingResult,
                                           Principal principal) throws AuthException {
        Optional<User> userOpt = userService.findByName(principal.getName());
        if(userOpt.isPresent()) {
            Comment newComment = commentService.save(comment, userOpt.get(), spotId);
            return Map.of("id", newComment.getId());
        } else {
            throw new AuthException("No principle");
        }

    }

    //Получить комментарии спота по id спота
    @GetMapping("/get-by-spot-id/{spotId}")
    public List<CommentDto> getBySpotId(@PathVariable Long spotId) {
        return commentService.findByCommentedSpotId(spotId).stream().map(dtoConverter::commentToDto).toList();
    }
}
