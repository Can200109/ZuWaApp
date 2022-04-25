package com.example.zuwaapp.entity;

import lombok.Data;

@Data
public class Rent {
    private String rentId;//自动生成
    private String rentName;
    private String rentDescribe;
    private Double rentPrice;
    private String rentPhoto;
    private String rentType;
    private String phoneNumber;//外键
    private String ownerPhoneNumber;
    private String productId;//外键


    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public String getRentName() {
        return rentName;
    }

    public void setRentName(String rentName) {
        this.rentName = rentName;
    }

    public String getRentDescribe() {
        return rentDescribe;
    }

    public void setRentDescribe(String rentDescribe) {
        this.rentDescribe = rentDescribe;
    }

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getRentPhoto() {
        return rentPhoto;
    }

    public void setRentPhoto(String rentPhoto) {
        this.rentPhoto = rentPhoto;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public Rent(String rentName, String rentDescribe, Double rentPrice, String rentPhoto, String rentType, String phoneNumber, String ownerPhoneNumber, String productId) {
        this.rentName = rentName;
        this.rentDescribe = rentDescribe;
        this.rentPrice = rentPrice;
        this.rentPhoto = rentPhoto;
        this.rentType = rentType;
        this.phoneNumber = phoneNumber;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.productId = productId;
    }

    public Rent() {
    }

    public Rent(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



}
