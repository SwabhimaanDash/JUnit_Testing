package com.inc.OrderProcessing.ComplianceEngine.service;

import com.company.order.model.Customer;
import com.company.order.model.Order;
import com.company.order.service.PricingService;
import com.company.order.service.TestDataFactory;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PricingService unit tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @ParameterizedTest(name = "total={0}, premium={1}, festival={2} => discount={3}")
    @CsvFileSource(resources = "/pricing-discount-rules.csv", numLinesToSkip = 1)
    @DisplayName("01 - should calculate discount percentage correctly")
    void shouldCalculateDiscountPercentage(
            double orderTotal,
            boolean premiumCustomer,
            boolean festivalEnabled,
            double expectedDiscount
    ) {
        Customer customer = new Customer();
        customer.setPremium(premiumCustomer);

        Order order = TestDataFactory.createOrder(orderTotal, customer, festivalEnabled);

        double discount = pricingService.calculateDiscountPercentage(order);

        assertEquals(
                expectedDiscount,
                discount,
                0.01,
                "Discount percentage mismatch"
        );

        assertTrue(
                discount <= 25,
                "Discount must not exceed 25% cap"
        );
    }

    @Test
    @DisplayName("02 - should apply GST after discount with rounding")
    void shouldCalculateGstCorrectly() {
        double discountedAmount = 10000.75;

        double gst = pricingService.calculateGst(discountedAmount);

        assertEquals(
                1800.14,
                gst,
                0.01,
                "GST must be 18% applied after discount with rounding"
        );
    }
}
