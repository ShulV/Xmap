package com.shulpov.spots_app.services;

import com.shulpov.spots_app.user.User;
import com.shulpov.spots_app.user.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public UserService(ImageInfoService imageInfoService, UserRepository userRepository) {
        this.imageInfoService = imageInfoService;
        this.userRepository = userRepository;
    }

    //Найти пользователя по id
    public Optional<User> findById(Long id) {
        logger.atInfo().log("findById id={}", id);
        return userRepository.findById(id);
    }

    //Найти пользователя по email
    public Optional<User> findByEmail(String email) {
        logger.atInfo().log("findByEmail email={}", email);
        return userRepository.findByEmail(email);
    }

    //Найти пользователя по имени
    public Optional<User> findByName(String name) {
        logger.atInfo().log("findByName name={}", name);
        return userRepository.findByName(name);
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
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //Найти пользователя по номеру телефона
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        logger.atInfo().log("findByPhoneNumber phoneNumber={}", phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber);
    }

}
