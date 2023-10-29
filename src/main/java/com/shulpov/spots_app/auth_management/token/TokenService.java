package com.shulpov.spots_app.auth_management.token;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }

    public Optional<Token> getTokenByValue(String token) {
        return tokenRepository.findByValue(token);
    }

    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

    public long count() {
        return tokenRepository.count();
    }

    public void deleteAllTokens(List<Token> tokens) {
        tokenRepository.deleteAll(tokens);
    }
}
