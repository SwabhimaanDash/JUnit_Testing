package com.inc.OrderProcessing.ComplianceEngine.service;

import com.company.order.exception.ComplianceViolationException;
import com.company.order.model.Order;
import com.company.order.service.ComplianceService;
import com.company.order.service.TestDataFactory;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ComplianceService unit tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ComplianceServiceTest {

    private ComplianceService complianceService;

    @BeforeEach
    void setUp() {
        complianceService = new ComplianceService();
    }

    @Test
    @DisplayName("01 - should fail when customer is inactive")
    void shouldFailWhenCustomerInactive() {
        Order order = TestDataFactory.validOrder();
        order.getCustomer().setActive(false);

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Inactive customer must fail compliance"
        );
    }

    @Test
    @DisplayName("02 - should fail when order has no items")
    void shouldFailWhenNoItems() {
        Order order = TestDataFactory.validOrder();
        order.getItems().clear();

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Order without items must fail compliance"
        );
    }

    @Test
    @DisplayName("03 - should fail when item price is invalid")
    void shouldFailWhenItemPriceInvalid() {
        Order order = TestDataFactory.validOrder();
        order.getItems().get(0).setPrice(0);

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Item price must be greater than zero"
        );
    }

    @Test
    @DisplayName("04 - should fail when quantity is invalid")
    void shouldFailWhenQuantityInvalid() {
        Order order = TestDataFactory.validOrder();
        order.getItems().get(0).setQuantity(0);

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Quantity must be greater than zero"
        );
    }

    @Test
    @DisplayName("05 - should fail when order total exceeds limit")
    void shouldFailWhenOrderTotalExceedsLimit() {
        Order order = TestDataFactory.orderWithTotal(600000);

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Order exceeding ₹5,00,000 must fail compliance"
        );
    }

    @Test
    @DisplayName("06 - should pass for valid order")
    void shouldPassForValidOrder() {
        Order order = TestDataFactory.validOrder();

        assertDoesNotThrow(
                () -> complianceService.validate(order),
                "Valid order must pass compliance"
        );
    }
}
