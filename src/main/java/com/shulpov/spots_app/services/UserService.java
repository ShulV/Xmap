package com.shulpov.spots_app.services;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ImageInfoService imageInfoService;
    private final UserRepo userRepo;

    @Autowired
    public UserService(ImageInfoService imageInfoService, UserRepo userRepo) {
        this.imageInfoService = imageInfoService;
        this.userRepo = userRepo;
    }

    //Найти пользователя по id
    public Optional<User> findById(Long id) {
        logger.atInfo().log("findById id={}", id);
        return userRepo.findById(id);
    }

    //Найти пользователя по email
    public Optional<User> findByEmail(String email) {
        logger.atInfo().log("findByEmail email={}", email);
        return userRepo.findByEmail(email);
    }

    //Найти пользователя по имени
    public Optional<User> findByName(String name) {
        logger.atInfo().log("findByName name={}", name);
        return userRepo.findByName(name);
    }

    //Удалить пользователя по id
    @Transactional(rollbackFor = IOException.class)
    public Boolean deleteById(Long id) {
        logger.atInfo().log("deleteById id={}", id);
        Optional<User> userOpt = findById(id);
        if(userOpt.isPresent()) {
            userOpt.get().getImageInfos().forEach(imageInfo -> {
                try {
                    imageInfoService.deleteUserImage(imageInfo.getId());
                } catch (IOException e) {
                    logger.atInfo().log("deleteById id={}; imageInfo with id={} not deleted", id, imageInfo.getId());
                    throw new RuntimeException(e);
                }
            });
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    //Найти пользователя по номеру телефона
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        logger.atInfo().log("findByPhoneNumber phoneNumber={}", phoneNumber);
        return userRepo.findByPhoneNumber(phoneNumber);
    }

}
