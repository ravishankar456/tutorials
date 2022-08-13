package com.rs.nbc;

public class Order {
    private int orderId;
    private String orderName;
    private boolean isPaid;

    public Order(int orderId) {
        this.orderId = orderId;
    }

    public Order(int orderId, String orderName) {
        this.orderName = orderName;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}
