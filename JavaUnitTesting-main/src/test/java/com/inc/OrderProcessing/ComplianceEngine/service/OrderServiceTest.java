package com.inc.OrderProcessing.ComplianceEngine.service;

import com.company.order.exception.InvalidOrderException;
import com.company.order.exception.OrderNotFoundException;
import com.company.order.model.Order;
import com.company.order.model.OrderStatus;
import com.company.order.repository.OrderRepository;
import com.company.order.service.ComplianceService;
import com.company.order.service.OrderServiceImpl;
import com.company.order.service.PricingService;
import com.company.order.service.TestDataFactory;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("OrderServiceImpl unit tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PricingService pricingService;

    @Mock
    private ComplianceService complianceService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("01 - should create order successfully")
    void shouldCreateOrderSuccessfully() {
        Order order = TestDataFactory.validOrder();

        when(pricingService.calculateDiscountPercentage(order)).thenReturn(10.0);
        when(pricingService.calculateGst(anyDouble())).thenReturn(180.0);
        when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        assertEquals(
                OrderStatus.CREATED,
                createdOrder.getStatus(),
                "Order must be in CREATED state"
        );

        verify(complianceService).validate(order);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("02 - should cancel order successfully")
    void shouldCancelOrderSuccessfully() {
        Order order = TestDataFactory.createdOrder();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder(1L, "Customer request");

        assertEquals(
                OrderStatus.CANCELLED,
                order.getStatus(),
                "Order must be cancelled"
        );

        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("03 - should fail when order not found")
    void shouldFailWhenOrderNotFound() {
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.cancelOrder(99L, "Invalid"),
                "Cancelling non-existing order must fail"
        );
    }

    @Test
    @DisplayName("04 - should fail when order not in CREATED state")
    void shouldFailWhenOrderNotInCreatedState() {
        Order order = TestDataFactory.shippedOrder();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidOrderException.class,
                () -> orderService.cancelOrder(1L, "Invalid"),
                "Only CREATED orders can be cancelled"
        );
    }

    @Test
    @DisplayName("05 - should fail when cancellation reason missing")
    void shouldFailWhenReasonMissing() {
        Order order = TestDataFactory.createdOrder();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidOrderException.class,
                () -> orderService.cancelOrder(1L, null),
                "Cancellation reason is mandatory"
        );
    }
}

