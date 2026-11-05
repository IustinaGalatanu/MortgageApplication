package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.product.IcrRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IcrRuleTest {

    private final IcrRule rule = new IcrRule();

    private MortgageApplication application;
    private MortgageProduct product;

    @BeforeEach
    void setUp() {
        application = new MortgageApplication();
        application.setApplicantType(ApplicantType.INDIVIDUAL);
        application.setLoanAmount(new BigDecimal("120000"));
        application.setExpectedMonthlyRent(new BigDecimal("1015"));

        product = new MortgageProduct();
        product.setFixedPeriod(2);
        product.setPayRate(new BigDecimal("0.05"));
        product.setAllowedApplicantTypes(Set.of(
                ApplicantType.INDIVIDUAL,
                ApplicantType.LIMITED_COMPANY
        ));
        product.setMinimumIcrByApplicantType(Map.of(
                ApplicantType.INDIVIDUAL, new BigDecimal("1.45"),
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")
        ));
    }

    @Test
    void shouldAcceptRentAboveMinimum() {
        application.setExpectedMonthlyRent(new BigDecimal("1015.01"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldAcceptRentExactlyAtMinimumForTwoYearProduct() {
        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectRentOnePennyBelowMinimum() {
        application.setExpectedMonthlyRent(new BigDecimal("1014.99"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.INSUFFICIENT_RENTAL_COVERAGE,
                reason.getRejectionType()
        );
        assertEquals(
                "Annual rent of GBP 12179.88 is lower than the minimum "
                        + "annual rent of GBP 12180.0000 "
                        + "required by the product's ICR",
                reason.getMessage()
        );
    }

    @Test
    void shouldRejectZeroRent() {
        application.setExpectedMonthlyRent(BigDecimal.ZERO);

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.INSUFFICIENT_RENTAL_COVERAGE,
                reason.getRejectionType()
        );
    }

    @Test
    void shouldAcceptRentAtMinimumStressRateFloor() {
        product.setPayRate(new BigDecimal("0.03"));
        application.setExpectedMonthlyRent(new BigDecimal("797.50"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectRentBelowMinimumStressRateFloor() {
        product.setPayRate(new BigDecimal("0.03"));
        application.setExpectedMonthlyRent(new BigDecimal("797.49"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.INSUFFICIENT_RENTAL_COVERAGE,
                reason.getRejectionType()
        );
    }

    @Test
    void shouldAcceptWhenPayRatePlusMarginEqualsStressRateFloor() {
        product.setPayRate(new BigDecimal("0.035"));
        application.setExpectedMonthlyRent(new BigDecimal("797.50"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldUsePayRateDirectlyForFiveYearProduct() {
        product.setFixedPeriod(5);
        product.setPayRate(new BigDecimal("0.055"));
        application.setExpectedMonthlyRent(new BigDecimal("797.50"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectRentBelowMinimumForFiveYearProduct() {
        product.setFixedPeriod(5);
        product.setPayRate(new BigDecimal("0.055"));
        application.setExpectedMonthlyRent(new BigDecimal("797.49"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.INSUFFICIENT_RENTAL_COVERAGE,
                reason.getRejectionType()
        );
    }

    @Test
    void shouldNotApplyStressRateFloorToFiveYearProduct() {
        product.setFixedPeriod(5);
        product.setPayRate(new BigDecimal("0.04"));
        application.setExpectedMonthlyRent(new BigDecimal("580"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldUseLimitedCompanyIcr() {
        application.setApplicantType(ApplicantType.LIMITED_COMPANY);
        application.setExpectedMonthlyRent(new BigDecimal("875"));

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldRejectLimitedCompanyRentBelowMinimum() {
        application.setApplicantType(ApplicantType.LIMITED_COMPANY);
        application.setExpectedMonthlyRent(new BigDecimal("874.99"));

        RejectionReason reason =
                rule.evaluate(application, product).orElseThrow();

        assertEquals(
                RejectionType.INSUFFICIENT_RENTAL_COVERAGE,
                reason.getRejectionType()
        );
    }

    @Test
    void shouldSkipIcrWhenApplicantTypeIsNotSupported() {
        product.setAllowedApplicantTypes(
                Set.of(ApplicantType.LIMITED_COMPANY)
        );
        product.setMinimumIcrByApplicantType(Map.of(
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")
        ));
        application.setExpectedMonthlyRent(BigDecimal.ZERO);

        assertTrue(rule.evaluate(application, product).isEmpty());
    }

    @Test
    void shouldThrowWhenIcrIsMissingForSupportedApplicantType() {
        product.setMinimumIcrByApplicantType(Map.of(
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")
        ));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> rule.evaluate(application, product)
        );

        assertEquals("Missing minimum Icr", exception.getMessage());
    }

    @Test
    void shouldThrowWhenFixedPeriodIsUnsupported() {
        product.setFixedPeriod(3);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> rule.evaluate(application, product)
        );

        assertEquals("Unsupported fixed period", exception.getMessage());
    }
}