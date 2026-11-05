package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.product.LoanAmountRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanAmountRuleTest {

    private final LoanAmountRule rule = new LoanAmountRule();

    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();

        product = new MortgageProduct();
        product.setMinLoanAmount(new BigDecimal("50000"));
        product.setMaxLoanAmount(new BigDecimal("1000000"));
    }

    @Test
    void shouldRejectLoanOnePennyBelowMinimum() {
        application.setLoanAmount(new BigDecimal("49999.99"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.LOAN_AMOUNT_BELOW_MINIMUM,
                reason.getRejectionType()
        );
        assertTrue(reason.getMessage().contains("49999.99"));
        assertTrue(reason.getMessage().contains("50000"));
    }

    @Test
    void shouldAcceptLoanExactlyAtMinimum() {
        application.setLoanAmount(new BigDecimal("50000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptLoanBetweenMinimumAndMaximum() {
        application.setLoanAmount(new BigDecimal("150000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptLoanExactlyAtMaximum() {
        application.setLoanAmount(new BigDecimal("1000000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectLoanOnePennyAboveMaximum() {
        application.setLoanAmount(new BigDecimal("1000000.01"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.LOAN_AMOUNT_ABOVE_MAXIMUM,
                reason.getRejectionType()
        );
        assertTrue(reason.getMessage().contains("1000000.01"));
        assertTrue(reason.getMessage().contains("GBP1000000"));
    }

    @Test
    void shouldAcceptMinimumWithDifferentDecimalScale() {
        application.setLoanAmount(new BigDecimal("50000.00"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptMaximumWithDifferentDecimalScale() {
        application.setLoanAmount(new BigDecimal("1000000.00"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }
}