package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class IcrRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        BigDecimal stressRate;
        if (mortgageProduct.getFixedPeriod() == 2) {
            stressRate = mortgageProduct.getPayRate().add(new BigDecimal("0.02")).max(new BigDecimal("0.055"));
        } else if (mortgageProduct.getFixedPeriod() == 5) {
            stressRate = mortgageProduct.getPayRate();
        } else {
            throw new IllegalStateException("Unsupported fixed period");
        }

        if (!mortgageProduct.getAllowedApplicantTypes().contains(application.getApplicantType())) {
            return Optional.empty();
        }

        BigDecimal minIcr = mortgageProduct.getMinimumIcrByApplicantType().get(application.getApplicantType());

        if (minIcr == null) {
            throw new IllegalStateException("Missing minimum Icr");
        }

        BigDecimal annualRent = application.getExpectedMonthlyRent().multiply(new BigDecimal("12"));
        BigDecimal minAnnualRent = minIcr.multiply(application.getLoanAmount()).multiply(stressRate);

        if (annualRent.compareTo(minAnnualRent) >= 0) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.INSUFFICIENT_RENTAL_COVERAGE);
        reason.setMessage("Annual rent of GBP " + annualRent.toPlainString() + " is lower than the minimum annual rent of GBP " +
                minAnnualRent.toPlainString() + " required by the product's ICR");
        return Optional.of(reason);
    }
}
