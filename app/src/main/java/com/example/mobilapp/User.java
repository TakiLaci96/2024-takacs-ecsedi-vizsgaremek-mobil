package com.example.mobilapp;

/** A felhasználó adatait tároló osztály */
public class User {
    private String name;
    private String email;
    private String password;
    //plusz egyéb adattagok ha szükséges

    // Regisztrációhoz szükséges adatok
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Bejelentkezéshez szükséges adatok
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String fullName) {
        this.name = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
