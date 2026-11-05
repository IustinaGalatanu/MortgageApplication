package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LocationRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (mortgageProduct.getSupportedLocations().contains(application.getPropertyLocation())) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.LOCATION_NOT_SUPPORTED);
        reason.setMessage("Location is " + application.getPropertyLocation() + " but need to be one of these " +
                mortgageProduct.getSupportedLocations());


        return Optional.of(reason);
    }
}
