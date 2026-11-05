package com.example.mortgagematching.service.rule.funder;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.FunderRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class FunderLtvRule implements FunderRule {

    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder) {

        BigDecimal maxLoanAmountLtv = application.getPropertyValue().multiply(funder.getMaxLtv());

        if (application.getLoanAmount().compareTo(maxLoanAmountLtv) <= 0) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.LTV_EXCEEDED);
        reason.setMessage("Request loan of " + application.getLoanAmount().toPlainString() +
                " exceeds the maximum of GBP " + maxLoanAmountLtv.toPlainString() +
                " permitted by this funder's LTV limit");
        return Optional.of(reason);
    }
}
