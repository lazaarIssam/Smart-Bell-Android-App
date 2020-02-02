package com.example.arduinomobileapp;

public class User {
    String idmac;
    String username;
    String password;
    String token;

    public User() {
    }

    public User(String idmac, String username, String password, String token) {
        this.idmac = idmac;
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String getIdmac() {
        return idmac;
    }

    public void setIdmac(String idmac) {
        this.idmac = idmac;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
