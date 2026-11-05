package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;

import java.util.Optional;

public class MinPropertyValueRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (application.getPropertyValue()
                .compareTo(mortgageProduct.getMinPropertyValue()) >= 0) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(
                RejectionType.PROPERTY_VALUE_BELOW_MINIMUM);
        reason.setMessage(
                "Property value of GBP "
                        + application.getPropertyValue().toPlainString()
                        + " is below the minimum of GBP "
                        + mortgageProduct.getMinPropertyValue().toPlainString()
                        + " required by product "
                        + mortgageProduct.getProductId());

        return Optional.of(reason);
    }
}
