package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto;

public class TokenResponse {
    private String token;
    private long expiresIn;
    private String username;

    public TokenResponse() {
    }

    public TokenResponse(String token, long expiresIn, String username) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
