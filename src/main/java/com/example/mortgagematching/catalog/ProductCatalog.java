package com.example.mortgagematching.catalog;

import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RepaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ProductCatalog {

    private final List<MortgageProduct> productList;

    public ProductCatalog() {

        MortgageProduct standardTwoYears = new MortgageProduct();
        standardTwoYears.setProductId("STANDARD_2Y_75");
        standardTwoYears.setPropertyType(PropertyType.STANDARD);
        standardTwoYears.setFixedPeriod(2);
        standardTwoYears.setPayRate(new BigDecimal("0.05"));
        standardTwoYears.setMaxLtv(new BigDecimal("0.75"));
        standardTwoYears.setExperienceRequired(false);
        standardTwoYears.setAllowedApplicantTypes(Set.of(ApplicantType.INDIVIDUAL, ApplicantType.LIMITED_COMPANY));
        standardTwoYears.setAllowedRepaymentTypes(Set.of(RepaymentType.INTEREST_ONLY, RepaymentType.CAPITAL_AND_INTEREST));
        standardTwoYears.setMinPropertyValue(new BigDecimal(65000));
        standardTwoYears.setSupportedLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES, PropertyLocation.SCOTLAND));
        standardTwoYears.setMinLoanAmount(new BigDecimal(50000));
        standardTwoYears.setMaxLoanAmount(new BigDecimal(1000000));
        standardTwoYears.setMinimumIcrByApplicantType(Map.of(ApplicantType.INDIVIDUAL, new BigDecimal("1.45"),
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")));

        MortgageProduct standardFiveYears = new MortgageProduct();
        standardFiveYears.setProductId("STANDARD_5Y_75");
        standardFiveYears.setPropertyType(PropertyType.STANDARD);
        standardFiveYears.setFixedPeriod(5);
        standardFiveYears.setPayRate(new BigDecimal("0.055"));
        standardFiveYears.setMaxLtv(new BigDecimal("0.75"));
        standardFiveYears.setExperienceRequired(false);
        standardFiveYears.setAllowedApplicantTypes(Set.of(ApplicantType.INDIVIDUAL, ApplicantType.LIMITED_COMPANY));
        standardFiveYears.setAllowedRepaymentTypes(Set.of(RepaymentType.INTEREST_ONLY, RepaymentType.CAPITAL_AND_INTEREST));
        standardFiveYears.setMinPropertyValue(new BigDecimal(65000));
        standardFiveYears.setSupportedLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES, PropertyLocation.SCOTLAND));
        standardFiveYears.setMinLoanAmount(new BigDecimal(50000));
        standardFiveYears.setMaxLoanAmount(new BigDecimal(1000000));
        standardFiveYears.setMinimumIcrByApplicantType(Map.of(ApplicantType.INDIVIDUAL, new BigDecimal("1.45"),
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")));

        MortgageProduct hmoFiveYears = new MortgageProduct();
        hmoFiveYears.setProductId("HMO_5Y_75");
        hmoFiveYears.setPropertyType(PropertyType.HMO);
        hmoFiveYears.setFixedPeriod(5);
        hmoFiveYears.setPayRate(new BigDecimal("0.06"));
        hmoFiveYears.setMaxLtv(new BigDecimal("0.75"));
        hmoFiveYears.setExperienceRequired(true);
        hmoFiveYears.setAllowedApplicantTypes(Set.of(ApplicantType.INDIVIDUAL, ApplicantType.LIMITED_COMPANY));
        hmoFiveYears.setAllowedRepaymentTypes(Set.of(RepaymentType.INTEREST_ONLY));
        hmoFiveYears.setMinPropertyValue(new BigDecimal(120000));
        hmoFiveYears.setSupportedLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES));
        hmoFiveYears.setMinLoanAmount(new BigDecimal(50000));
        hmoFiveYears.setMaxLoanAmount(new BigDecimal(1000000));
        hmoFiveYears.setMinimumIcrByApplicantType(Map.of(ApplicantType.INDIVIDUAL, new BigDecimal("1.45"),
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")));

        MortgageProduct mufbFiveYears = new MortgageProduct();
        mufbFiveYears.setProductId("MUFB_5Y_70");
        mufbFiveYears.setPropertyType(PropertyType.MUFB);
        mufbFiveYears.setFixedPeriod(5);
        mufbFiveYears.setPayRate(new BigDecimal("0.06"));
        mufbFiveYears.setMaxLtv(new BigDecimal("0.7"));
        mufbFiveYears.setExperienceRequired(true);
        mufbFiveYears.setAllowedApplicantTypes(Set.of(ApplicantType.INDIVIDUAL, ApplicantType.LIMITED_COMPANY));
        mufbFiveYears.setAllowedRepaymentTypes(Set.of(RepaymentType.INTEREST_ONLY));
        mufbFiveYears.setMinPropertyValue(new BigDecimal(120000));
        mufbFiveYears.setSupportedLocations(Set.of(PropertyLocation.ENGLAND, PropertyLocation.WALES));
        mufbFiveYears.setMinLoanAmount(new BigDecimal(50000));
        mufbFiveYears.setMaxLoanAmount(new BigDecimal(1000000));
        mufbFiveYears.setMinimumIcrByApplicantType(Map.of(ApplicantType.INDIVIDUAL, new BigDecimal("1.45"),
                ApplicantType.LIMITED_COMPANY, new BigDecimal("1.25")));


        productList = List.of(standardTwoYears, standardFiveYears, hmoFiveYears, mufbFiveYears);
    }

    public List<MortgageProduct> getProductList() {
        return productList;
    }
}
