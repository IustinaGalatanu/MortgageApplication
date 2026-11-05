package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepaymentRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (mortgageProduct.getAllowedRepaymentTypes().contains(application.getRepaymentType())) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.REPAYMENT_TYPE_NOT_SUPPORTED);
        reason.setMessage("This repayment type " + application.getRepaymentType() +
                " is not supported. The supported one are " + mortgageProduct.getAllowedRepaymentTypes());

        return Optional.of(reason);
    }
}
