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
public class LtvRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        BigDecimal maxLoanAmountLtv = application.getPropertyValue().multiply(mortgageProduct.getMaxLtv());
        if (application.getLoanAmount().compareTo(maxLoanAmountLtv) <= 0) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.LTV_EXCEEDED);
        reason.setMessage("Request loan of " + application.getLoanAmount().toPlainString() +
                " exceeds the maximum of GBP " + maxLoanAmountLtv.toPlainString() +
                " permitted by this product's LTV limit");

        return Optional.of(reason);

    }

}
