package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.funder.FunderApplicantTypeRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FunderApplicantTypeRuleTest {

    private final FunderApplicantTypeRule rule =
            new FunderApplicantTypeRule();

    private MortgageApplication application;
    private MortgageProduct product;
    private Funder funder;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        product = new MortgageProduct();

        funder = new Funder();
        funder.setFunderId("TEST_FUNDER");
        funder.setAllowedApplicantTypes(Set.of(
                ApplicantType.INDIVIDUAL,
                ApplicantType.LIMITED_COMPANY
        ));
    }

    @Test
    void shouldAcceptIndividualWhenSupported() {
        application.setApplicantType(ApplicantType.INDIVIDUAL);

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldAcceptLimitedCompanyWhenSupported() {
        application.setApplicantType(ApplicantType.LIMITED_COMPANY);

        assertTrue(rule.evaluate(application, product, funder).isEmpty());
    }

    @Test
    void shouldRejectIndividualWhenNotSupported() {
        application.setApplicantType(ApplicantType.INDIVIDUAL);
        funder.setAllowedApplicantTypes(
                Set.of(ApplicantType.LIMITED_COMPANY)
        );

        RejectionReason reason =
                rule.evaluate(application, product, funder).orElseThrow();

        assertEquals(
                RejectionType.APPLICANT_TYPE_NOT_SUPPORTED,
                reason.getRejectionType()
        );
        assertEquals(
                "Applicant type INDIVIDUAL is not supported by funder "
                        + "TEST_FUNDER. Supported applicant types: [LIMITED_COMPANY]",
                reason.getMessage()
        );
    }

    @Test
    void shouldRejectLimitedCompanyWhenNotSupported() {
        application.setApplicantType(ApplicantType.LIMITED_COMPANY);
        funder.setAllowedApplicantTypes(
                Set.of(ApplicantType.INDIVIDUAL)
        );

        RejectionReason reason =
                rule.evaluate(application, product, funder).orElseThrow();

        assertEquals(
                RejectionType.APPLICANT_TYPE_NOT_SUPPORTED,
                reason.getRejectionType()
        );
        assertEquals(
                "Applicant type LIMITED_COMPANY is not supported by funder "
                        + "TEST_FUNDER. Supported applicant types: [INDIVIDUAL]",
                reason.getMessage()
        );
    }
}