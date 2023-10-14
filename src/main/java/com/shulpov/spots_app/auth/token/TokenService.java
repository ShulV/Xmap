package com.shulpov.spots_app.auth.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Optional<Token> getTokenByValue(String token) {
        return tokenRepository.findByValue(token);
    }

    public void deleteTokenByValue(String value) {
        tokenRepository.deleteByValue(value);
    }

    @Transactional(readOnly = true)
    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }
}
