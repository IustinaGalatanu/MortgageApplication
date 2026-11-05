package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.product.LandlordRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LandlordRuleTest {

    private final LandlordRule rule = new LandlordRule();

    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        product = new MortgageProduct();
    }

    @Test
    void shouldRejectWhenExperienceIsRequiredButApplicantHasNone() {
        product.setExperienceRequired(true);
        application.setHasLandlordExperience(false);

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.LANDLORD_EXPERIENCE_REQUIRED,
                reason.getRejectionType()
        );
        assertEquals(
                "This product requires landlord experience, but this applicant has none",
                reason.getMessage()
        );
    }

    @Test
    void shouldAcceptWhenExperienceIsRequiredAndApplicantHasExperience() {
        product.setExperienceRequired(true);
        application.setHasLandlordExperience(true);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptWhenExperienceIsNotRequiredAndApplicantHasNone() {
        product.setExperienceRequired(false);
        application.setHasLandlordExperience(false);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptWhenExperienceIsNotRequiredAndApplicantHasExperience() {
        product.setExperienceRequired(false);
        application.setHasLandlordExperience(true);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }
}