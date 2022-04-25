package com.example.zuwaapp.entity;

import lombok.Data;

@Data
public class Product {
    private String productId;//自动生成
    private String productPhoto;

    private String productName;
    private String productDescribe;
    private Double productPrice;
    private Double productDeposit;//押金
    private Integer productCount;
    private String productType;
    private String phoneNumber;
    private int productRent;
    private String rentPhoneNumber;

    public Product(){}

    public Product(String productPhoto, String productDescribe) {
        this.productPhoto = productPhoto;
        this.productDescribe = productDescribe;
    }

    public Product(String productName, String productDescribe, Double productPrice, Double productDeposit, String productType, String phoneNumber) {
        this.productName = productName;
        this.productDescribe = productDescribe;
        this.productPrice = productPrice;
        this.productDeposit = productDeposit;
        this.productType = productType;
        this.phoneNumber = phoneNumber;
        this.productCount = 0;
        this.productRent = 0;
        this.rentPhoneNumber = "";
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescribe() {
        return productDescribe;
    }

    public void setProductDescribe(String productDescribe) {
        this.productDescribe = productDescribe;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getProductDeposit() {
        return productDeposit;
    }

    public void setProductDeposit(Double productDeposit) {
        this.productDeposit = productDeposit;
    }

    public int getProductRent() {
        return productRent;
    }

    public void setProductRent(int productRent) {
        this.productRent = productRent;
    }

    public String getRentPhoneNumber() {
        return rentPhoneNumber;
    }

    public void setRentPhoneNumber(String rentPhoneNumber) {
        this.rentPhoneNumber = rentPhoneNumber;
    }
}
