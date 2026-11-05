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
public class FunderApplicantTypeRule implements FunderRule {
    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder) {

        if (funder.getAllowedApplicantTypes().contains(application.getApplicantType())) {
            return Optional.empty();
        }

        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(RejectionType.APPLICANT_TYPE_NOT_SUPPORTED);
        reason.setMessage("Applicant type " + application.getApplicantType() + " is not supported by funder " + funder.getFunderId()
                + ". Supported applicant types: " + funder.getAllowedApplicantTypes()
        );
        return Optional.of(reason);
    }
}
