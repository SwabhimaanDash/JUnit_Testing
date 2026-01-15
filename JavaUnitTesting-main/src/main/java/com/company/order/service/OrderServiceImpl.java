package com.company.order.service;

import com.company.order.exception.InvalidOrderException;
import com.company.order.exception.OrderNotFoundException;
import com.company.order.model.Order;
import com.company.order.model.OrderStatus;
import com.company.order.repository.OrderRepository;

public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final PricingService pricingService;
    private final ComplianceService complianceService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            PricingService pricingService,
            ComplianceService complianceService
    ) {
        this.orderRepository = orderRepository;
        this.pricingService = pricingService;
        this.complianceService = complianceService;
    }

    public Order createOrder(Order order) {
        complianceService.validate(order);

        double discount = pricingService.calculateDiscountPercentage(order);
        double discountedAmount = order.getTotalAmount() * (1 - discount / 100);
        pricingService.calculateGst(discountedAmount);

        order.setStatus(OrderStatus.CREATED);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long orderId, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidOrderException("Cancellation reason required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
