package com.example.arduinomobileapp;

public class Information {
    String date;
    String home;

    public Information() {
    }

    public Information(String date, String home) {
        this.date = date;
        this.home = home;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}
