package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.funder.FunderLtvRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FunderLtvRuleTest {

    private final FunderLtvRule rule = new FunderLtvRule();

    private MortgageApplication application;
    private MortgageProduct product;
    private Funder funder;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        application.setPropertyValue(new BigDecimal("200000"));

        product = new MortgageProduct();
        product.setMaxLtv(new BigDecimal("0.75"));

        funder = new Funder();
        funder.setMaxLtv(new BigDecimal("0.65"));
    }

    @Test
    void shouldAcceptLoanBelowFunderLtvLimit() {
        application.setLoanAmount(new BigDecimal("129999.99"));

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldAcceptLoanExactlyAtFunderLtvLimit() {
        application.setLoanAmount(new BigDecimal("130000"));

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldAcceptLoanAtLimitWithDifferentDecimalScale() {
        application.setLoanAmount(new BigDecimal("130000.0000"));

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldRejectLoanOnePennyAboveFunderLtvLimit() {
        application.setLoanAmount(new BigDecimal("130000.01"));

        RejectionReason reason =
                rule.evaluate(application, product, funder).orElseThrow();

        assertEquals(
                RejectionType.LTV_EXCEEDED,
                reason.getRejectionType()
        );
        assertEquals(
                "Request loan of 130000.01 exceeds the maximum of GBP "
                        + "130000.00 permitted by this funder's LTV limit",
                reason.getMessage()
        );
    }

    @Test
    void shouldRejectLoanWithinProductLimitButAboveFunderLimit() {
        application.setLoanAmount(new BigDecimal("140000"));

        RejectionReason reason =
                rule.evaluate(application, product, funder).orElseThrow();

        assertEquals(
                RejectionType.LTV_EXCEEDED,
                reason.getRejectionType()
        );
    }

    @Test
    void shouldUseFunderLimitEvenWhenProductLimitIsLower() {
        product.setMaxLtv(new BigDecimal("0.60"));
        application.setLoanAmount(new BigDecimal("130000"));

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }
}