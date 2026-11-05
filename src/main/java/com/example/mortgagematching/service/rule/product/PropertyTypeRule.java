package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PropertyTypeRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (application.getPropertyType() == mortgageProduct.getPropertyType()) {
            return Optional.empty();
        }
        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.PROPERTY_TYPE_NOT_SUPPORTED);
        reason.setMessage("Property type " + application.getPropertyType() + " is not supported.Required property type: "
                + mortgageProduct.getPropertyType());
        return Optional.of(reason);
    }
}
