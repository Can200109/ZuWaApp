package com.example.zuwaapp.entity;

import lombok.Data;


@Data
public class OrderType {
    private String orderId;
    private String rentPhone;
    private String type;
    private String productPhone;
    private String productId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRentPhone() {
        return rentPhone;
    }

    public void setRentPhone(String rentPhone) {
        this.rentPhone = rentPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductPhone() {
        return productPhone;
    }

    public void setProductPhone(String productPhone) {
        this.productPhone = productPhone;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public OrderType() {
    }

    public OrderType(String rentPhone, String type) {
        this.rentPhone = rentPhone;
        this.type = type;
    }

    public OrderType(String rentPhone, String type, String productPhone, String productId) {
        this.rentPhone = rentPhone;
        this.type = type;
        this.productPhone = productPhone;
        this.productId = productId;
    }

    public OrderType(String rentPhone, String type, String productId) {
        this.rentPhone = rentPhone;
        this.type = type;
        this.productId = productId;
    }
}
