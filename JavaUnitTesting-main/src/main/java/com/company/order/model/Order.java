package com.company.order.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private Customer customer;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;
    private boolean festivalEnabled;
    private double totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isFestivalEnabled() {
        return festivalEnabled;
    }

    public void setFestivalEnabled(boolean festivalEnabled) {
        this.festivalEnabled = festivalEnabled;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
