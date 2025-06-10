package com.example.myapplication.model;

public class Msg {
    public long id;
    public String message;
    public Msg(long id, String message) {
        this.id = id;
        this.message = message;
    }
    @Override
    public String toString() {
        return message;
    }
}