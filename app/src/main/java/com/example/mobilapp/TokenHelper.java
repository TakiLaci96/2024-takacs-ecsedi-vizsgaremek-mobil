package com.example.mobilapp;

/**
 * A token kezelésére szolgáló osztály
 */
public class TokenHelper {
    private String token;

    public TokenHelper(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
