package com.company.order.service;

import com.company.order.exception.ComplianceViolationException;
import com.company.order.model.Order;
import com.company.order.model.OrderItem;

public class ComplianceService {

    public void validate(Order order) {

        if (!order.getCustomer().isActive()) {
            throw new ComplianceViolationException("Customer is inactive");
        }

        if (order.getItems().isEmpty()) {
            throw new ComplianceViolationException("Order has no items");
        }

        for (OrderItem item : order.getItems()) {
            if (item.getPrice() <= 0) {
                throw new ComplianceViolationException("Invalid item price");
            }
            if (item.getQuantity() <= 0) {
                throw new ComplianceViolationException("Invalid item quantity");
            }
        }

        if (order.getTotalAmount() > 500000) {
            throw new ComplianceViolationException("Order exceeds max limit");
        }
    }
}
