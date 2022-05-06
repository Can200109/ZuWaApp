package com.example.zuwaapp.entity;

import android.content.Intent;

/**
 * Created by x_heng.
 * <p>
 * Date: 2022/5/3
 */
public class UserState {
    private Integer id;
    private String userMode;
    private String userPhoneById;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserMode() {
        return userMode;
    }

    public void setUserMode(String userMode) {
        this.userMode = userMode;
    }

    public String getUserPhoneById() {
        return userPhoneById;
    }

    public void setUserPhoneById(String userPhoneById) {
        this.userPhoneById = userPhoneById;
    }

    public UserState(String userMode, String userPhoneById) {
        this.userMode = userMode;
        this.userPhoneById = userPhoneById;
    }

    public UserState() {
    }
}
