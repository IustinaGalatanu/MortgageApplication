package com.example.mortgagematching.model;

import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class Funder {

    private String funderId;
    private Set<String> supportedProductIds;
    private BigDecimal maxLtv;
    private Set<ApplicantType> allowedApplicantTypes;
    private Set<PropertyLocation> allowedPropertyLocations;
}
