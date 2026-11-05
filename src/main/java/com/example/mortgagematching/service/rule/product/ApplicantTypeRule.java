package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicantTypeRule implements ProductRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (mortgageProduct.getAllowedApplicantTypes().contains(application.getApplicantType())) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.APPLICANT_TYPE_NOT_SUPPORTED);
        reason.setMessage("Applicant type " + application.getApplicantType() + " is not supported by product " + mortgageProduct.getProductId()
                + ". Supported applicant types: " + mortgageProduct.getAllowedApplicantTypes());

        return Optional.of(reason);
    }
}
