package com.example.smartpds.model;

public class Order {
    public Order(String orderId, String totalAmount, String customer, String distributor, String orderPlaced) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.distributor = distributor;
        this.orderPlaced = orderPlaced;
    }

    String orderId,totalAmount,customer,distributor,orderPlaced;
}
