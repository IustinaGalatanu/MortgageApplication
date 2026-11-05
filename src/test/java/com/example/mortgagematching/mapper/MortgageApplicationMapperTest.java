package com.example.mortgagematching.mapper;

import com.example.mortgagematching.dto.MortgageApplicationRequest;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RepaymentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MortgageApplicationMapperTest {

    private final MortgageApplicationMapper mapper = new MortgageApplicationMapper();

    @Test
    void shouldMapAllFields() {
        MortgageApplicationRequest request = new MortgageApplicationRequest();
        request.setCaseReference("CASE-42");
        request.setLoanAmount(new BigDecimal("150000.50"));
        request.setPropertyValue(new BigDecimal("250000"));
        request.setExpectedMonthlyRent(new BigDecimal("1800"));
        request.setPropertyType(PropertyType.HMO);
        request.setApplicantType(ApplicantType.LIMITED_COMPANY);
        request.setPropertyLocation(PropertyLocation.WALES);
        request.setHasLandlordExperience(true);
        request.setRepaymentType(RepaymentType.CAPITAL_AND_INTEREST);

        MortgageApplication application = mapper.toApplication(request);

        assertEquals("CASE-42", application.getCaseReference());
        assertEquals(new BigDecimal("150000.50"), application.getLoanAmount());
        assertEquals(new BigDecimal("250000"), application.getPropertyValue());
        assertEquals(new BigDecimal("1800"), application.getExpectedMonthlyRent());
        assertEquals(PropertyType.HMO, application.getPropertyType());
        assertEquals(ApplicantType.LIMITED_COMPANY, application.getApplicantType());
        assertEquals(PropertyLocation.WALES, application.getPropertyLocation());
        assertTrue(application.isHasLandlordExperience());
        assertEquals(RepaymentType.CAPITAL_AND_INTEREST, application.getRepaymentType());
    }

    @Test
    void shouldMapFalseLandlordExperience() {
        MortgageApplicationRequest request = new MortgageApplicationRequest();
        request.setHasLandlordExperience(false);

        MortgageApplication application = mapper.toApplication(request);

        assertFalse(application.isHasLandlordExperience());
    }
}
