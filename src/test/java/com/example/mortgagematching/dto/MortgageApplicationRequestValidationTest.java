package com.example.mortgagematching.dto;

import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RepaymentType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MortgageApplicationRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequestHasNoViolations() {
        assertTrue(validator.validate(validRequest()).isEmpty());
    }

    @Test
    void missingLoanAmountIsRejected() {
        MortgageApplicationRequest request = validRequest();
        request.setLoanAmount(null);

        assertEquals(1, validator.validate(request).size());
    }

    @Test
    void negativeLoanAmountIsRejected() {
        MortgageApplicationRequest request = validRequest();
        request.setLoanAmount(new BigDecimal("-1"));

        assertEquals(1, validator.validate(request).size());
    }

    @Test
    void blankCaseReferenceIsRejected() {
        MortgageApplicationRequest request = validRequest();
        request.setCaseReference(" ");

        assertEquals(1, validator.validate(request).size());
    }

    @Test
    void zeroRentIsAccepted() {
        MortgageApplicationRequest request = validRequest();
        request.setExpectedMonthlyRent(BigDecimal.ZERO);

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void moreThanTwoDecimalPlacesIsRejected() {
        MortgageApplicationRequest request = validRequest();
        request.setLoanAmount(new BigDecimal("150000.123"));

        assertEquals(1, validator.validate(request).size());
    }

    private MortgageApplicationRequest validRequest() {
        MortgageApplicationRequest request = new MortgageApplicationRequest();
        request.setCaseReference("CASE-1");
        request.setLoanAmount(new BigDecimal("150000"));
        request.setPropertyValue(new BigDecimal("250000"));
        request.setExpectedMonthlyRent(new BigDecimal("2000"));
        request.setPropertyType(PropertyType.STANDARD);
        request.setApplicantType(ApplicantType.INDIVIDUAL);
        request.setPropertyLocation(PropertyLocation.ENGLAND);
        request.setHasLandlordExperience(true);
        request.setRepaymentType(RepaymentType.INTEREST_ONLY);
        return request;
    }
}

