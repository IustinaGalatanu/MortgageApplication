package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoanAmountRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (mortgageProduct.getMaxLoanAmount().compareTo(application.getLoanAmount()) >= 0 &&
                mortgageProduct.getMinLoanAmount().compareTo(application.getLoanAmount()) <= 0) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        if (mortgageProduct.getMaxLoanAmount().compareTo(application.getLoanAmount()) < 0) {
            reason.setRejectionType(RejectionType.LOAN_AMOUNT_ABOVE_MAXIMUM);
            reason.setMessage("Loan amount " + application.getLoanAmount() + " exceeds the maximum of GBP"
                    + mortgageProduct.getMaxLoanAmount());
        } else if (mortgageProduct.getMinLoanAmount().compareTo(application.getLoanAmount()) > 0) {
            reason.setRejectionType(RejectionType.LOAN_AMOUNT_BELOW_MINIMUM);
            reason.setMessage("Loan amount " + application.getLoanAmount() + " is below the minimum of GBP"
                    + mortgageProduct.getMinLoanAmount());
        }
        return Optional.of(reason);
    }
}
