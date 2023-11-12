package com.shulpov.spots_app.comments;

import com.shulpov.spots_app.comments.utils.CommentDtoConverter;
import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/comments")
@Tag(name="Контроллер комментариев", description="Отвечает за добавление и получение комментариев мест для катания")

public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentDtoConverter commentDtoConverter;

    @Autowired
    public CommentController(
            CommentService commentService, @Lazy UserService userService, CommentDtoConverter commentDtoConverter) {
        this.commentService = commentService;
        this.userService = userService;
        this.commentDtoConverter = commentDtoConverter;
    }

    @Operation(
            summary = "Добавление комментария к месту для катания",
            description = "Позволяет пользователю добавить комментарий",
            security = @SecurityRequirement(name = "accessTokenAuth")
    )
    @Transactional
    @PostMapping("/add-comment-by-spot-id/{spotId}")
    public Map<String, Object> getBySpotId(@PathVariable Long spotId,
                                           @RequestBody @Valid Comment comment,
                                           BindingResult bindingResult,
                                           Principal principal) throws AuthException {
        logger.atInfo().log("/add-comment-by-spot-id/{}", spotId);
        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if(userOpt.isPresent()) {
            Comment newComment = commentService.save(comment, userOpt.get(), spotId);
            logger.atInfo().log("new comment");
            return Map.of("id", newComment.getId());
        } else {
            logger.atError().log("not created comment");
            throw new AuthException("No principle");
        }

    }

    @Operation(
            summary = "Удалить комментарий по id",
            description = "Позволяет пользователю удалить свой комментарий по id комментария"
    )
    @DeleteMapping("/{commentId}")
    public Map<Object, Object> deleteById(@PathVariable Long commentId, Principal principal) throws AuthException {
        logger.atInfo().log("/delete-by-id/{}", commentId);
        Optional<User> userOpt = userService.findByEmail(principal.getName());
        if(userOpt.isPresent()) {
            boolean commentExists = userOpt.get().getComments().stream()
                    .filter(c-> Objects.equals(c.getId(), commentId)).toList()
                    .size() == 1;
            logger.atInfo().log("commentExists={}", commentExists);
            if(commentExists) {
                commentService.deleteById(commentId);
                logger.atInfo().log("comment was deleted");
                return Map.of("message", "комментарий удален", "id", commentId);
            } else {
                logger.atError().log("comment wasn't deleted");
                return Map.of("message", "комментарий с таким id не найден", "id", commentId);
            }
        } else {
            throw new AuthException("User not found");
        }
    }

    @Operation(
            summary = "Получение всех комментариев места для катания",
            description = "Позволяет пользователю получить все комментарии"
    )
    @GetMapping("/get-by-spot-id/{spotId}")
    public List<CommentDto> getBySpotId(@PathVariable Long spotId) {
        logger.atInfo().log("/get-by-spot-id/{}", spotId);
        return commentService.findByCommentedSpotId(spotId).stream().map(commentDtoConverter::commentToDto).toList();
    }
}
