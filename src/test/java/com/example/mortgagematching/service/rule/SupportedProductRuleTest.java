
package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.service.rule.funder.SupportedProductRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportedProductRuleTest {

    private SupportedProductRule rule;
    private MortgageApplication application;
    private Funder funder;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        rule = new SupportedProductRule();
        application = mock(MortgageApplication.class);
        funder = mock(Funder.class);
        product = mock(MortgageProduct.class);
    }

    @Test
    void shouldPassWhenProductIsSupported() {
        when(product.getProductId())
                .thenReturn("STANDARD_2Y_75");

        when(funder.getSupportedProductIds())
                .thenReturn(Set.of("STANDARD_2Y_75", "STANDARD_5Y_75"));

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldRejectWhenProductIsNotSupported() {
        when(product.getProductId())
                .thenReturn("HMO_5Y_75");

        when(funder.getSupportedProductIds())
                .thenReturn(Set.of("STANDARD_2Y_75", "STANDARD_5Y_75"));

        assertTrue(rule.evaluate(application, product, funder).isPresent());
    }

    @Test
    void shouldRejectWhenNoProductsAreSupported() {
        when(product.getProductId())
                .thenReturn("STANDARD_2Y_75");

        when(funder.getSupportedProductIds())
                .thenReturn(Set.of());

        assertTrue(rule.evaluate(application, product, funder).isPresent());
    }
}
