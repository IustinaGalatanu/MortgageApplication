package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.product.ApplicantTypeRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantTypeRuleTest {

    private final ApplicantTypeRule rule = new ApplicantTypeRule();

    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        product = new MortgageProduct();
        product.setProductId("TEST_PRODUCT");
        product.setAllowedApplicantTypes(Set.of(
                ApplicantType.INDIVIDUAL,
                ApplicantType.LIMITED_COMPANY
        ));
    }

    @Test
    void shouldAcceptIndividualWhenSupported() {
        application.setApplicantType(ApplicantType.INDIVIDUAL);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptLimitedCompanyWhenSupported() {
        application.setApplicantType(ApplicantType.LIMITED_COMPANY);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectIndividualWhenNotSupported() {
        application.setApplicantType(ApplicantType.INDIVIDUAL);
        product.setAllowedApplicantTypes(
                Set.of(ApplicantType.LIMITED_COMPANY)
        );

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.APPLICANT_TYPE_NOT_SUPPORTED,
                reason.getRejectionType()
        );
        assertEquals(
                "Applicant type INDIVIDUAL is not supported by product "
                        + "TEST_PRODUCT. Supported applicant types: [LIMITED_COMPANY]",
                reason.getMessage()
        );
    }

    @Test
    void shouldRejectLimitedCompanyWhenNotSupported() {
        application.setApplicantType(ApplicantType.LIMITED_COMPANY);
        product.setAllowedApplicantTypes(
                Set.of(ApplicantType.INDIVIDUAL)
        );

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.APPLICANT_TYPE_NOT_SUPPORTED,
                reason.getRejectionType()
        );
        assertEquals(
                "Applicant type LIMITED_COMPANY is not supported by product "
                        + "TEST_PRODUCT. Supported applicant types: [INDIVIDUAL]",
                reason.getMessage()
        );
    }
}