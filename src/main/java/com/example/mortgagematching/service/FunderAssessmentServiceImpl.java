package com.example.mortgagematching.service;

import com.example.mortgagematching.model.*;
import com.example.mortgagematching.service.rule.FunderRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FunderAssessmentServiceImpl implements FunderAssessmentService {

    List<FunderRule> rules;

    public FunderAssessmentServiceImpl(List<FunderRule> rule) {
        this.rules = rule;
    }

    @Override
    public FunderAssesment assess(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder) {

        List<RejectionReason> reasons = new ArrayList<>();

        for (FunderRule rule : rules) {
            Optional<RejectionReason> rejectionReason = rule.evaluate(application, mortgageProduct, funder);

            if (rejectionReason.isPresent()) {
                reasons.add(rejectionReason.get());
            }
        }

        FunderAssesment assesment = new FunderAssesment();
        assesment.setFunderId(funder.getFunderId());
        assesment.setEligibleFunder(reasons.isEmpty());
        assesment.setRejectionReasons(reasons);

        return assesment;
    }
}
