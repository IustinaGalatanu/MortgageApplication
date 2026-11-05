package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.product.LtvRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LtvRuleTest {

    private final LtvRule rule = new LtvRule();

    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        application.setPropertyValue(new BigDecimal("200000"));

        product = new MortgageProduct();
        product.setMaxLtv(new BigDecimal("0.75"));
    }

    @Test
    void shouldAcceptLoanBelowLtvLimit() {
        application.setLoanAmount(new BigDecimal("149999.99"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptLoanExactlyAtLtvLimit() {
        application.setLoanAmount(new BigDecimal("150000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectLoanOnePennyAboveLtvLimit() {
        application.setLoanAmount(new BigDecimal("150000.01"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.LTV_EXCEEDED,
                reason.getRejectionType()
        );
        assertEquals(
                "Request loan of 150000.01 exceeds the maximum of GBP "
                        + "150000.00 permitted by this product's LTV limit",
                reason.getMessage()
        );
    }

    @Test
    void shouldAcceptLoanAtLimitWithDifferentDecimalScale() {
        application.setLoanAmount(new BigDecimal("150000.0000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptLoanExactlyAtSeventyPercentLimit() {
        product.setMaxLtv(new BigDecimal("0.70"));
        application.setLoanAmount(new BigDecimal("140000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectLoanAboveSeventyPercentLimit() {
        product.setMaxLtv(new BigDecimal("0.70"));
        application.setLoanAmount(new BigDecimal("140000.01"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.LTV_EXCEEDED,
                reason.getRejectionType()
        );
    }

    @Test
    void shouldNotRoundUpCalculatedLtvLimit() {
        application.setPropertyValue(new BigDecimal("200000.01"));
        application.setLoanAmount(new BigDecimal("150000.01"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.LTV_EXCEEDED,
                reason.getRejectionType()
        );
    }
}