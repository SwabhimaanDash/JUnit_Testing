package com.company.order.service;

import com.company.order.model.Order;

public class PricingService {

    public double calculateDiscountPercentage(Order order) {
        double discount = 0;

        double total = order.getTotalAmount();

        if (total >= 25000) discount += 10;
        else if (total >= 10000) discount += 5;

        if (order.getCustomer().isPremium()) discount += 5;
        if (order.isFestivalEnabled()) discount += 5;

        return Math.min(discount, 25);
    }

    public double calculateGst(double discountedAmount) {
        return Math.round(discountedAmount * 0.18 * 100.0) / 100.0;
    }
}
