
package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.service.rule.product.MinPropertyValueRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MinPropertyValueRuleTest {

    private MinPropertyValueRule rule;
    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        rule = new MinPropertyValueRule();
        application = mock(MortgageApplication.class);
        product = mock(MortgageProduct.class);

        when(product.getMinPropertyValue())
                .thenReturn(new BigDecimal("100000"));
    }

    @Test
    void shouldRejectWhenPropertyValueIsBelowMinimum() {
        when(application.getPropertyValue())
                .thenReturn(new BigDecimal("99999"));

        assertTrue(rule.evaluate(application, product).isPresent());
    }

    @Test
    void shouldPassWhenPropertyValueEqualsMinimum() {
        when(application.getPropertyValue())
                .thenReturn(new BigDecimal("100000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldPassWhenPropertyValueIsAboveMinimum() {
        when(application.getPropertyValue())
                .thenReturn(new BigDecimal("150000"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }
}
