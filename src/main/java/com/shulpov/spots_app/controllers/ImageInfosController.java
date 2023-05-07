package com.shulpov.spots_app.controllers;

import com.shulpov.spots_app.dto.ImageInfoDto;
import com.shulpov.spots_app.models.ImageInfo;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.ImageInfoService;
import com.shulpov.spots_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/image-service")
public class ImageInfosController {
    private final ImageInfoService imageInfoService;
    private final UserService userService;

    @Autowired
    public ImageInfosController(ImageInfoService imageInfoService, UserService userService) {
        this.imageInfoService = imageInfoService;
        this.userService = userService;
    }

    //Загрузить картинку пользователя
    @PostMapping("/upload-user-image")
    public ResponseEntity<Map<String, Long>> uploadUserImage(@RequestParam MultipartFile file, Principal principal) {
        Optional<User> user = userService.findByName(principal.getName());
        if(user.isPresent()) {
            try {
                ImageInfo imageInfo = imageInfoService.uploadUserImage(file, user.get());
                return new ResponseEntity<>(Map.of("id", imageInfo.getId()), HttpStatus.CREATED);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    //Скачать картинку пользователя по id
    @GetMapping(path = "/download-user-image/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadUserImage(@PathVariable("id") Long id) throws IOException {
        Optional<ImageInfo> foundFile = imageInfoService.findById(id);
        if (foundFile.isPresent()) {
            String genName = foundFile.get().getGenName();
            Resource resource = imageInfoService.downloadUserImage(genName);
            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "file; filename=" +
                                    foundFile.get().getOriginalName())
                    .body(resource);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Удалить картинку пользователя
    @DeleteMapping("/delete-user-image/{id}")
    public ResponseEntity<Map<String, Long>> deleteUserImage(@PathVariable("id") Long id) {
        try {
            Long delUserId = imageInfoService.deleteUserImage(id);
            return new ResponseEntity<>(Map.of("id", delUserId), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
