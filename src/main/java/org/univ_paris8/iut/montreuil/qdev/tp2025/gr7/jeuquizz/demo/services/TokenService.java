package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenService {
    // Stockage temporaire en mémoire des jetons associés aux utilisateurs
    private static final Map<String, User> tokens = new ConcurrentHashMap<>();

    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, user);
        return token;
    }

    public User validateToken(String token) {
        return tokens.get(token);
    }

    public void invalidateToken(String token) {
        tokens.remove(token);
    }
}
