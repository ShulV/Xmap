package com.shulpov.spots_app.auth.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<Token> getTokenByValue(String token) {
        return tokenRepository.findByValue(token);
    }

    @Transactional
    public void deleteTokenByValue(String value) {
        tokenRepository.deleteByValue(value);
    }

    //пока что используется только для тестов
    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }
}
