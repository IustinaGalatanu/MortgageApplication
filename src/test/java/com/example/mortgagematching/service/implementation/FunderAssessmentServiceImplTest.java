package com.example.mortgagematching.service.implementation;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.FunderAssesment;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.service.FunderAssessmentServiceImpl;
import com.example.mortgagematching.service.rule.FunderRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FunderAssessmentServiceImplTest {

    private FunderRule rule1;
    private FunderRule rule2;
    private FunderAssessmentServiceImpl service;
    private MortgageApplication application;
    private MortgageProduct product;
    private Funder funder;
    private RejectionReason rejectionReason1;
    private RejectionReason rejectionReason2;

    @BeforeEach
    void setUp() {
        rule1 = mock(FunderRule.class);
        rule2 = mock(FunderRule.class);
        service = new FunderAssessmentServiceImpl(List.of(rule1, rule2));
        application = mock(MortgageApplication.class);
        product = mock(MortgageProduct.class);
        funder = mock(Funder.class);
        rejectionReason1 = mock(RejectionReason.class);
        rejectionReason2 = mock(RejectionReason.class);
    }

    @Test
    void shouldReturnEligibleFunderWhenAllRulesPass() {
        when(funder.getFunderId()).thenReturn("FUNDER_A");
        when(rule1.evaluate(application, product, funder)).thenReturn(Optional.empty());
        when(rule2.evaluate(application, product, funder)).thenReturn(Optional.empty());

        FunderAssesment result = service.assess(application, product, funder);

        assertEquals("FUNDER_A", result.getFunderId());
        assertTrue(result.isEligibleFunder());
        assertTrue(result.getRejectionReasons().isEmpty());

        verify(rule1).evaluate(application, product, funder);
        verify(rule2).evaluate(application, product, funder);
    }

    @Test
    void shouldReturnIneligibleFunderWhenRulesFail() {
        when(funder.getFunderId()).thenReturn("FUNDER_B");
        when(rule1.evaluate(application, product, funder)).thenReturn(Optional.of(rejectionReason1));
        when(rule2.evaluate(application, product, funder)).thenReturn(Optional.of(rejectionReason2));

        FunderAssesment result = service.assess(application, product, funder);

        assertEquals("FUNDER_B", result.getFunderId());
        assertFalse(result.isEligibleFunder());
        assertEquals(List.of(rejectionReason1, rejectionReason2), result.getRejectionReasons());

        verify(rule1).evaluate(application, product, funder);
        verify(rule2).evaluate(application, product, funder);
    }
}