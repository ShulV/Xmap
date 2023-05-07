package com.shulpov.spots_app.services;

import com.shulpov.spots_app.file_manager.FileManager;
import com.shulpov.spots_app.models.ImageInfo;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.ImageInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ImageInfoService {
    @Value("${file-storage.images.users}")
    private String usersUploadPath;

    @Value("${file-storage.images.spots}")
    private String spotsUploadPath;

    private final ImageInfoRepo imageInfoRepo;
    private final FileManager imageManager;

    @Autowired
    public ImageInfoService(ImageInfoRepo imageInfoRepo, FileManager imageManager) {
        this.imageInfoRepo = imageInfoRepo;
        this.imageManager = imageManager;
    }

    //Сгенерировать название изображения
    private String generateName(String name) {
        return (UUID.randomUUID().toString() + LocalDate.now().toString() + name).trim();
    }

    public Optional<ImageInfo> findById(Long id) {
        return imageInfoRepo.findById(id);
    }

    //Загрузить картинку пользователя
    @Transactional(rollbackFor = {IOException.class})
    public ImageInfo uploadUserImage(MultipartFile file, User user) throws IOException {
        String genName = generateName(file.getOriginalFilename());
        ImageInfo createdFile = new ImageInfo();
        createdFile.setOriginalName(file.getOriginalFilename());
        createdFile.setGenName(genName);
        createdFile.setSize((int) file.getSize());
        createdFile.setUploadDate(LocalDate.now());
        createdFile.setUser(user);

        createdFile = imageInfoRepo.save(createdFile);
        imageManager.upload(file.getBytes(), usersUploadPath, genName);

        return createdFile;
    }

    //Скачать картинку пользователя
    @Transactional(rollbackFor = {IOException.class})
    public Resource downloadUserImage(String genName) throws IOException{
        return imageManager.download(usersUploadPath, genName);
    }

    //Удалить картинку пользователя
    @Transactional(rollbackFor = {IOException.class})
    public Long deleteUserImage(Long id) throws Exception {
        Optional<ImageInfo> file = imageInfoRepo.findById(id);
        if (file.isPresent()) {
            imageInfoRepo.delete(file.get());
            imageManager.delete(usersUploadPath, file.get().getGenName());
            return file.get().getId();
        }
        throw new Exception("Данные картинки не найдены в БД");
    }
}
