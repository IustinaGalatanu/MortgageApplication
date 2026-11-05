package com.example.mortgagematching.service.implementation;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.ProductAssesment;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.service.ProductAssessmentServiceImpl;
import com.example.mortgagematching.service.rule.ProductRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductAssessmentServiceImplTest {

    private ProductRule rule1;
    private ProductRule rule2;
    private ProductAssessmentServiceImpl service;
    private MortgageApplication application;
    private MortgageProduct product;
    private RejectionReason rejectionReason1;
    private RejectionReason rejectionReason2;

    @BeforeEach
    void setUp() {
        rule1 = mock(ProductRule.class);
        rule2 = mock(ProductRule.class);
        service = new ProductAssessmentServiceImpl(List.of(rule1, rule2));
        application = mock(MortgageApplication.class);
        product = mock(MortgageProduct.class);
        rejectionReason1 = mock(RejectionReason.class);
        rejectionReason2 = mock(RejectionReason.class);
    }

    @Test
    void shouldReturnEligibleProductWhenAllRulesPass() {
        when(product.getProductId()).thenReturn("STANDARD_2Y_75");
        when(rule1.evaluate(application, product)).thenReturn(Optional.empty());
        when(rule2.evaluate(application, product)).thenReturn(Optional.empty());

        ProductAssesment result = service.assess(application, product);

        assertEquals("STANDARD_2Y_75", result.getProductId());
        assertTrue(result.isEligibleProduct());
        assertTrue(result.getRejectionReasons().isEmpty());
        assertTrue(result.getFunderAssesments().isEmpty());

        verify(rule1).evaluate(application, product);
        verify(rule2).evaluate(application, product);
    }

    @Test
    void shouldReturnIneligibleProductWhenRulesFail() {
        when(product.getProductId()).thenReturn("HMO_5Y_75");
        when(rule1.evaluate(application, product)).thenReturn(Optional.of(rejectionReason1));
        when(rule2.evaluate(application, product)).thenReturn(Optional.of(rejectionReason2));

        ProductAssesment result = service.assess(application, product);

        assertEquals("HMO_5Y_75", result.getProductId());
        assertFalse(result.isEligibleProduct());
        assertEquals(List.of(rejectionReason1, rejectionReason2), result.getRejectionReasons());
        assertTrue(result.getFunderAssesments().isEmpty());

        verify(rule1).evaluate(application, product);
        verify(rule2).evaluate(application, product);
    }
}