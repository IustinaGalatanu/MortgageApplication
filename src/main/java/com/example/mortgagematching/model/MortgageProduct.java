package com.example.mortgagematching.model;

import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RepaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Data
public class MortgageProduct {

    private String productId;
    private PropertyType propertyType;
    private int fixedPeriod;
    private BigDecimal payRate;
    private BigDecimal maxLtv;
    private boolean experienceRequired;
    private Set<ApplicantType> allowedApplicantTypes;
    private Set<RepaymentType> allowedRepaymentTypes;
    private BigDecimal minPropertyValue;
    private Set<PropertyLocation> supportedLocations;
    private BigDecimal minLoanAmount;
    private BigDecimal maxLoanAmount;
    private Map<ApplicantType, BigDecimal> minimumIcrByApplicantType;
}
