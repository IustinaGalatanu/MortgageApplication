package com.example.mortgagematching.mapper;

import com.example.mortgagematching.dto.MortgageApplicationRequest;
import com.example.mortgagematching.model.MortgageApplication;
import org.springframework.stereotype.Component;

@Component
public class MortgageApplicationMapper {

    public MortgageApplication toApplication(MortgageApplicationRequest mortgageApplicationRequest) {

        MortgageApplication mortgageApplication = new MortgageApplication();
        mortgageApplication.setCaseReference(mortgageApplicationRequest.getCaseReference());
        mortgageApplication.setLoanAmount(mortgageApplicationRequest.getLoanAmount());
        mortgageApplication.setPropertyValue(mortgageApplicationRequest.getPropertyValue());
        mortgageApplication.setExpectedMonthlyRent(mortgageApplicationRequest.getExpectedMonthlyRent());
        mortgageApplication.setPropertyType(mortgageApplicationRequest.getPropertyType());
        mortgageApplication.setApplicantType(mortgageApplicationRequest.getApplicantType());
        mortgageApplication.setPropertyLocation(mortgageApplicationRequest.getPropertyLocation());
        mortgageApplication.setHasLandlordExperience(mortgageApplicationRequest.getHasLandlordExperience());
        mortgageApplication.setRepaymentType(mortgageApplicationRequest.getRepaymentType());

        return mortgageApplication;
    }
}
