
package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.service.rule.product.PropertyTypeRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyTypeRuleTest {

    private PropertyTypeRule rule;
    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        rule = new PropertyTypeRule();
        application = mock(MortgageApplication.class);
        product = mock(MortgageProduct.class);
    }

    @Test
    void shouldPassWhenPropertyTypeMatches() {
        when(application.getPropertyType())
                .thenReturn(PropertyType.STANDARD);

        when(product.getPropertyType())
                .thenReturn(PropertyType.STANDARD);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectWhenPropertyTypeDoesNotMatch() {
        when(application.getPropertyType())
                .thenReturn(PropertyType.HMO);

        when(product.getPropertyType())
                .thenReturn(PropertyType.STANDARD);

        assertTrue(rule.evaluate(application, product).isPresent());
    }
}
