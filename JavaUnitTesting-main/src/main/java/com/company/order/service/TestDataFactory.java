package com.company.order.service;

import com.company.order.model.*;

public class TestDataFactory {

    public static Order validOrder() {
        Order order = new Order();
        order.setCustomer(activeCustomer());
        order.getItems().add(validItem());
        order.setTotalAmount(20000);
        order.setFestivalEnabled(false);
        return order;
    }

    public static Order createdOrder() {
        Order order = validOrder();
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    public static Order shippedOrder() {
        Order order = validOrder();
        order.setStatus(OrderStatus.SHIPPED);
        return order;
    }

    public static Order orderWithTotal(double total) {
        Order order = validOrder();
        order.setTotalAmount(total);
        return order;
    }

    public static Order createOrder(double total, Customer customer, boolean festival) {
        Order order = new Order();
        order.setCustomer(customer);
        order.getItems().add(validItem());
        order.setTotalAmount(total);
        order.setFestivalEnabled(festival);
        return order;
    }

    private static Customer activeCustomer() {
        Customer customer = new Customer();
        customer.setActive(true);
        return customer;
    }

    private static OrderItem validItem() {
        OrderItem item = new OrderItem();
        item.setPrice(1000);
        item.setQuantity(2);
        return item;
    }
}
