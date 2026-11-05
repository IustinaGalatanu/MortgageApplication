
package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.enums.RepaymentType;
import com.example.mortgagematching.service.rule.product.RepaymentRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepaymentRuleTest {

    private RepaymentRule rule;
    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        rule = new RepaymentRule();
        application = mock(MortgageApplication.class);
        product = mock(MortgageProduct.class);
    }

    @Test
    void shouldPassWhenRepaymentTypeIsSupported() {
        when(application.getRepaymentType())
                .thenReturn(RepaymentType.INTEREST_ONLY);

        when(product.getAllowedRepaymentTypes())
                .thenReturn(Set.of(RepaymentType.INTEREST_ONLY));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectWhenRepaymentTypeIsNotSupported() {
        when(application.getRepaymentType())
                .thenReturn(RepaymentType.CAPITAL_AND_INTEREST);

        when(product.getAllowedRepaymentTypes())
                .thenReturn(Set.of(RepaymentType.INTEREST_ONLY));

        assertTrue(rule.evaluate(application, product).isPresent());
    }

    @Test
    void shouldPassWhenProductSupportsBothRepaymentTypes() {
        when(application.getRepaymentType())
                .thenReturn(RepaymentType.CAPITAL_AND_INTEREST);

        when(product.getAllowedRepaymentTypes())
                .thenReturn(Set.of(
                        RepaymentType.INTEREST_ONLY,
                        RepaymentType.CAPITAL_AND_INTEREST
                ));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }
}
