package com.example.recipe;

public class User {
    private String email;
    private String uid;

    public User(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "E-mail: " + getEmail() + "\nUID: " + getUid() + "\n";
    }
}
