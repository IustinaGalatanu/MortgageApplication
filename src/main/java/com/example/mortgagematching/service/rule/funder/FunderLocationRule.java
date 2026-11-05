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
public class FunderLocationRule implements FunderRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder) {

        if (funder.getAllowedPropertyLocations().contains(application.getPropertyLocation())) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.LOCATION_NOT_SUPPORTED);
        reason.setMessage("Location is " + application.getPropertyLocation() + " but need to be one of these " +
                funder.getAllowedPropertyLocations());

        return Optional.of(reason);
    }
}
