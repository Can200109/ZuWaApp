package com.example.zuwaapp.entity;

import lombok.Data;


@Data
public class User {
    private String userName;
    private String userPassword;
    private String phoneNumber;
    private String userPhoto;

    public User(String userName, String userPassword, String phoneNumber) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
