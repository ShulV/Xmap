package com.shulpov.spots_app.services;

import com.shulpov.spots_app.file_manager.FileManager;
import com.shulpov.spots_app.models.ImageInfo;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.repo.ImageInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class ImageInfoService {
    @Value("${file-storage.images.users}")
    private String usersUploadPath;

    @Value("${file-storage.images.spots}")
    private String spotsUploadPath;

    private final ImageInfoRepo imageInfoRepo;
    private final SpotService spotService;
    private final FileManager imageManager;
    private final Logger logger = LoggerFactory.getLogger(ImageInfoService.class);

    @Autowired
    public ImageInfoService(ImageInfoRepo imageInfoRepo, @Lazy SpotService spotService, FileManager imageManager) {
        this.imageInfoRepo = imageInfoRepo;
        this.spotService = spotService;
        this.imageManager = imageManager;
    }

    //Сгенерировать название изображения
    private String generateName(String name) {
        logger.atInfo().log(" generateName({})", name);
        String newName = (UUID.randomUUID().toString() + LocalDate.now().toString() + name).replaceAll(" ", "");
        logger.atInfo().log("new generated name: {}", newName);
        return newName;
    }

    //Получить информацию о картинке по её id
    public Optional<ImageInfo> findById(Long id) {
        return imageInfoRepo.findById(id);
    }

    //Загрузить картинку пользователя
    //P.s. используется в одном контроллере, там есть проверка на удаление СВОЕЙ картинки
    @Transactional(rollbackFor = NoTransactionException.class)
    public ImageInfo uploadUserImage(MultipartFile file, User user) throws IOException {
        logger.atInfo().log("uploadUserImage file.name={} user.name={}", file.getOriginalFilename(), user.getName());
        String genName = generateName(file.getOriginalFilename());
        ImageInfo createdFile = new ImageInfo();
        createdFile.setOriginalName(file.getOriginalFilename());
        createdFile.setGenName(genName);
        createdFile.setSize((int) file.getSize());
        createdFile.setUploadDate(new Date(System.currentTimeMillis()));
        createdFile.setPhotographedUser(user);

        createdFile = imageInfoRepo.save(createdFile);
        imageManager.upload(file.getBytes(), usersUploadPath, genName);
        logger.atInfo().log("uploadUserImage success " +
                        "file: id={} name={} genName={} uploadDate={}",
                createdFile.getId(),
                createdFile.getOriginalName(),
                createdFile.getGenName(),
                createdFile.getUploadDate());
        return createdFile;
    }

    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional(rollbackFor = NoTransactionException.class)
    public ImageInfo uploadSpotImage(MultipartFile file, Long spotId) throws IOException {
        logger.atInfo().log("uploadSpotImage file.name={} spotId={}", file.getOriginalFilename(), spotId);
        String genName = generateName(file.getOriginalFilename());
        ImageInfo createdFile = new ImageInfo();
        createdFile.setOriginalName(file.getOriginalFilename());
        createdFile.setGenName(genName);
        createdFile.setSize((int) file.getSize());
        createdFile.setUploadDate(new Date(System.currentTimeMillis()));
        Optional<Spot> spot = spotService.findById(spotId);
        if(spot.isPresent()) {
            createdFile.setPhotographedSpot(spot.get());
            createdFile = imageInfoRepo.save(createdFile);
            imageManager.upload(file.getBytes(), spotsUploadPath, genName);
            logger.atInfo().log("uploadSpotImage success " +
                            "file: id={} name={} genName={} uploadDate={}",
                    createdFile.getId(),
                    createdFile.getOriginalName(),
                    createdFile.getGenName(),
                    createdFile.getUploadDate());
            return createdFile;
        } else {
            throw new NoSuchObjectException("No such spot in DB spotId=" + spotId);
        }
    }

    //Скачать картинку пользователя
    public Resource downloadUserImage(String genName) throws IOException{
        logger.atInfo().log("downloadUserImage genName={}", genName);
        return imageManager.download(usersUploadPath, genName);
    }

    //Скачать картинку спота
    public Resource downloadSpotImage(String genName) throws IOException{
        logger.atInfo().log("downloadSpotImage genName={}", genName);
        return imageManager.download(spotsUploadPath, genName);
    }


    //Удалить картинку пользователя
    //P.s. используется в одном контроллере, там есть проверка на удаление СВОЕЙ картинки
    @Transactional(rollbackFor = NoTransactionException.class)
    public Long deleteUserImage(Long id) throws IOException {
        logger.atInfo().log("deleteUserImage id={}", id);
        Optional<ImageInfo> file = imageInfoRepo.findById(id);
        if (file.isPresent()) {
            logger.atInfo().log("deleteUserImage imageInfo with id={} exists", id);
            imageInfoRepo.deleteByIdWithoutRefs(id);
            imageManager.delete(usersUploadPath, file.get().getGenName());
            return id;
        }
        logger.atError().log("deleteUserImage imageInfo with id={} doesn't exist", id);
        throw new NotFoundException("Images not found in DB");
    }

    //Удалить картинку спота
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @Transactional(rollbackFor = NoTransactionException.class)
    public Long deleteSpotImage(Long id) throws IOException {
        logger.atInfo().log("deleteSpotImage id={}", id);
        Optional<ImageInfo> file = imageInfoRepo.findById(id);
        if (file.isPresent()) {
            logger.atInfo().log("deleteSpotImage imageInfo with id={} exists", id);
            imageInfoRepo.deleteByIdWithoutRefs(file.get().getId());
            imageManager.delete(spotsUploadPath, file.get().getGenName());
            return file.get().getId();
        }
        logger.atError().log("deleteSpotImage imageInfo with id={} doesn't exist", id);
        throw new NotFoundException("Images not found in DB");
    }

    //Проверка на наличие у юзера картинки с определенным id
    public boolean userHasImageWithId(User user, Long id) {
        logger.atInfo().log("userHasImageWithId user_id={} image_id={}", user.getId(), id);
        int size = user.getImageInfos().stream()
                .filter(el->Objects.equals(el.getId(), id)).toList().size();
        return size == 1;
    }

    //Проверка на наличие у спота картинки с определенным id
    public boolean spotHasImageWithId(Spot spot, Long id) {
        logger.atInfo().log("spotHasImageWithId spot_id={} image_id={}", spot.getId(), id);
        int size = spot.getImageInfos().stream()
                .filter(el->Objects.equals(el.getId(), id)).toList().size();
        return size == 1;
    }
}
