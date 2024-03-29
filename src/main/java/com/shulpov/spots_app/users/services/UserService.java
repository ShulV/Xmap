package com.shulpov.spots_app.users.services;

import com.shulpov.spots_app.authentication_management.services.JwtService;
import com.shulpov.spots_app.image_infos.ImageInfoService;
import com.shulpov.spots_app.users.UserRepository;
import com.shulpov.spots_app.users.dto.CommentatorDto;
import com.shulpov.spots_app.users.exception.UserNotFoundException;
import com.shulpov.spots_app.users.models.User;
import com.shulpov.spots_app.users.utils.UserDtoConverter;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
@Scope(value = "prototype")//TODO избавиться
@RequiredArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ImageInfoService imageInfoService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        logger.atInfo().log("findByEmail email={}", email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByName(String name) {
        logger.atInfo().log("findByName name={}", name);
        return userRepository.findByName(name);
    }

    @Transactional(rollbackFor = IOException.class)
    public Boolean deleteById(Long id) {
        logger.atInfo().log("deleteById id={}", id);
        Optional<User> userOpt = findById(id);
        if(userOpt.isPresent()) {
            if(userOpt.get().getImageInfos() != null) {
                userOpt.get().getImageInfos().forEach(imageInfo -> {
                    try {
                        imageInfoService.deleteUserImage(imageInfo.getId());
                    } catch (IOException e) {
                        logger.atError().log("deleteById id={}; imageInfo with id={} not deleted",
                                id, imageInfo.getId(), e);
                    }
                });
            }
            User user = userOpt.get();
            Hibernate.initialize(user.getTokens());
            userRepository.delete(user);

            return true;
        }
        return false;
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        logger.atInfo().log("findByPhoneNumber phoneNumber={}", phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public CommentatorDto getFullInfoByAccessToken(String accessHeader)
            throws UserNotFoundException, JwtException, AuthenticationCredentialsNotFoundException {
        String accessToken = jwtService.getAccessTokenFromAccessHeader(accessHeader);
        Optional<User> userOpt = getByAccessToken(accessToken);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User by access token not found");
        }
        return userDtoConverter.convertToCommentatorDto(userOpt.get());
    }

    /**
     * Получить пользователя по его access токену
     */
    private Optional<User> getByAccessToken(String accessToken) throws JwtException {
        String email = jwtService.extractEmail(accessToken);
        return userRepository.findByEmail(email);
    }
}
