package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.models.ImageInfo;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.ImageInfoService;
import com.shulpov.spots_app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/image-service")
@Tag(name="Контроллер информации об изображениях",
        description="Позволяет загружать, скачивать и удалять изображения для пользователей и мест для катания")
public class ImageInfoController {
    private final ImageInfoService imageInfoService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(ImageInfoController.class);

    @Autowired
    public ImageInfoController(ImageInfoService imageInfoService, @Lazy UserService userService) {
        this.imageInfoService = imageInfoService;
        this.userService = userService;
    }

    @Operation(
            summary = "Загрузка изображения для своего аккаунта",
            description = "Позволяет пользователю загрузить изображение для своего аккаунта"
    )
    @PostMapping("/upload-user-image")
    public ResponseEntity<Map<String, Long>> uploadUserImage(@RequestParam MultipartFile file, Principal principal) {
        logger.atInfo().log("/upload-user-image filename={} size={} principle.name={}",
                file.getOriginalFilename(), file.getSize(), principal.getName());
        Optional<User> user = userService.findByName(principal.getName());
        if(user.isPresent()) {
            try {
                ImageInfo imageInfo = imageInfoService.uploadUserImage(file, user.get());
                logger.atInfo().log("image info created in with id={}", imageInfo.getId());
                return new ResponseEntity<>(Map.of("id", imageInfo.getId()), HttpStatus.CREATED);
            } catch (IOException e) {
                logger.atInfo().log("/upload-user-image IOException 400");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            logger.atInfo().log("/upload-user-image 403");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Operation(
            summary = "Загрузка изображения для спота",
            description = "Позволяет модератору или админу добавить 1 изображение для спота"
    )
    @PostMapping("/upload-spot-image/{id}")
    public ResponseEntity<Map<String, Long>> uploadSpotImage(
            @PathVariable(name = "id") Long spotId, @RequestParam MultipartFile file, Principal principal) {
        logger.atInfo().log("/upload-spot-image filename={} size={} principle.name={}",
                file.getOriginalFilename(), file.getSize(), principal.getName());
        Optional<User> user = userService.findByName(principal.getName());
        if(user.isPresent()) {
            try {
                ImageInfo imageInfo = imageInfoService.uploadSpotImage(file, spotId);
                logger.atInfo().log("image info created in with id={}", imageInfo.getId());
                return new ResponseEntity<>(Map.of("id", imageInfo.getId()), HttpStatus.CREATED);
            } catch (IOException e) {
                logger.atInfo().log("/upload-spot-image IOException 400: filename={}, spotId={}",
                        file.getOriginalFilename(), spotId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            logger.atError().log("/upload-spot-image 403 (shouldn't working after filter)");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(
            summary = "Скачивание изображения пользователя по id изображения",
            description = "Позволяет пользователю скачивать изображение любого другого пользователя"
    )
    @GetMapping(path = "/download-user-image/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadUserImage(@PathVariable("id") Long id) throws IOException {
        logger.atInfo().log("/download-user-image/{}", id);
        Optional<ImageInfo> foundFile = imageInfoService.findById(id);
        if (foundFile.isPresent()) {
            String genName = foundFile.get().getGenName();
            logger.atInfo().log("/download-user-image/{} exists genName={}", id, genName);
            Resource resource = imageInfoService.downloadUserImage(genName);
            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "file; filename=" +
                                    foundFile.get().getOriginalName())
                    .body(resource);
        } else {
            logger.atInfo().log("/download-user-image/{} bad request", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/download-spot-image/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadSpotImage(@PathVariable("id") Long id) throws IOException {
        logger.atInfo().log("/download-spot-image/{}", id);
        Optional<ImageInfo> foundFile = imageInfoService.findById(id);
        if (foundFile.isPresent()) {
            String genName = foundFile.get().getGenName();
            logger.atInfo().log("/download-spot-image/{} exists genName={}", id, genName);
            Resource resource = imageInfoService.downloadSpotImage(genName);
            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "file; filename=" +
                                    foundFile.get().getOriginalName())
                    .body(resource);
        } else {
            logger.atInfo().log("/download-spot-image/{} bad request", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Удалить картинку пользователя
    @DeleteMapping("/delete-user-image/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserImage(@PathVariable("id") Long id, Principal principal) {
        logger.atInfo().log("/delete-user-image/{id}", id);
        try {
            Optional<User> user = userService.findByName(principal.getName());
            if(user.isPresent() && imageInfoService.userHasImageWithId(user.get(), id)) {
                Long delUserId = imageInfoService.deleteUserImage(id);
                return new ResponseEntity<>(Map.of("id", delUserId), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    Map.of("message","image with id=" + id + " for user=" + user.get().getName() + " not found"),
                    HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
