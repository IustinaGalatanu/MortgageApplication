package com.example.mortgagematching.service.rule.funder;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.FunderRule;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class SupportedProductRule implements FunderRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder) {

        if (funder.getSupportedProductIds().contains(mortgageProduct.getProductId())) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.PRODUCT_NOT_SUPPORTED);
        reason.setMessage("Product " + mortgageProduct.getProductId() + " is not supported by funder " + funder.getFunderId());
        return Optional.of(reason);
    }
}
