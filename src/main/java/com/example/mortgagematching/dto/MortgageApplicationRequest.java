package com.example.mortgagematching.dto;

import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RepaymentType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MortgageApplicationRequest {

    @NotBlank
    private String caseReference;

    @NotNull
    @Positive
    private BigDecimal loanAmount;

    @NotNull
    @Positive
    private BigDecimal propertyValue;

    @NotNull
    @PositiveOrZero
    private BigDecimal expectedMonthlyRent;

    @NotNull
    private PropertyType propertyType;

    @NotNull
    private ApplicantType applicantType;

    @NotNull
    private PropertyLocation propertyLocation;

    @NotNull
    private Boolean hasLandlordExperience;

    @NotNull
    private RepaymentType repaymentType;

    @AssertTrue(message =
            "Monetary amounts must be representable with at most two decimal places")
    public boolean isMonetaryPrecisionValid() {
        return hasValidPrecision(loanAmount) &&
                hasValidPrecision(propertyValue) &&
                hasValidPrecision(expectedMonthlyRent);

    }

    private static boolean hasValidPrecision(BigDecimal amount) {
        return amount == null || amount.stripTrailingZeros().scale() <= 2;

    }
}
