package com.shulpov.spots_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shulpov.spots_app.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secretJWT;

    private final static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    //Генерировать токен (время действия 7 дней)
    public String generateToken(User user) {
        logger.atInfo().log("generateToken user: id={} name={} email={} phoneNumber={} birthday={}" +
                " regDate={} role={}",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthday(),
                user.getRegDate(),
                user.getRoleCodeName());
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(7).toInstant());
        return JWT.create()
                .withSubject("User details")
                //Клеймы - пары ключ:значение, можно сделать много .withClaim пар
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withClaim("phone", user.getPhoneNumber())
                .withClaim("birthday", user.getBirthday().toString())
                .withClaim("registration_date", user.getRegDate().toString())
                //Когда токен выдан
                .withIssuedAt(new Date())
                //Кем токен выдан
                .withIssuer("Spring-app Shulpov")
                //Когда срок действия токена закончится
                .withExpiresAt(expirationDate)
                //Подпись, секретный ключ для шифрования и алгоритм шифрования
                .sign(Algorithm.HMAC256(secretJWT));

    }

    //Валидировать токен
    public String validateTokenAndRetrieveClaim(String token) {
        logger.atInfo().log("validateTokenAndRetrieveClaim(token) token={}", token);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretJWT))
                .withSubject("User details")
                .withIssuer("Spring-app Shulpov")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        logger.atInfo().log("validateTokenAndRetrieveClaim(token) success");
        return decodedJWT.getClaim("email").asString();
    }
}
