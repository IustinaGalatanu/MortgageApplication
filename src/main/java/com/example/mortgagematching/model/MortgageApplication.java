package com.example.mortgagematching.model;

import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RepaymentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MortgageApplication {

    private String caseReference;
    private BigDecimal loanAmount;
    private BigDecimal propertyValue;
    private BigDecimal expectedMonthlyRent;
    private PropertyType propertyType;
    private ApplicantType applicantType;
    private PropertyLocation propertyLocation;
    private boolean hasLandlordExperience;
    private RepaymentType repaymentType;

}
