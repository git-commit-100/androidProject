package com.example.bookworm.utilities;

public class Order {
    private CartItem orderItem;
    private double priceToPay;

    public Order(CartItem orderItem, double priceToPay) {
        this.orderItem = orderItem;
        this.priceToPay = priceToPay;
    }

    public CartItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(CartItem orderItem) {
        this.orderItem = orderItem;
    }

    public double getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(double priceToPay) {
        this.priceToPay = priceToPay;
    }
}
