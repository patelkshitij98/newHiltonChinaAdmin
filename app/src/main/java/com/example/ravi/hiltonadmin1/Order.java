package com.example.ravi.hiltonadmin1;

public class Order {
    String orderId;
    String userName;
    String address;
    String paid;

    public Order(String orderId, String userName, String address, String paid) {
        this.orderId = orderId;
        this.userName = userName;
        this.address = address;
        this.paid = paid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public String getPaid() {
        return paid;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
