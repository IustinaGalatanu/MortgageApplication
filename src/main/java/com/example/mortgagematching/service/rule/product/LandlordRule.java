package com.example.mortgagematching.service.rule.product;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LandlordRule implements ProductRule {

    @Override
    public Optional<RejectionReason> evaluate(MortgageApplication application, MortgageProduct mortgageProduct) {

        if (mortgageProduct.isExperienceRequired()
                && !application.isHasLandlordExperience()) {
            RejectionReason reason = new RejectionReason();
            reason.setRejectionType(RejectionType.LANDLORD_EXPERIENCE_REQUIRED);
            reason.setMessage("This product requires landlord experience, but this applicant has none");
            return Optional.of(reason);
        }
        return Optional.empty();
    }
}
