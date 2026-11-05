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

    public FunderAssessmentServiceImpl(List<FunderRule> rules) {
        this.rules = rules;
    }

    @Override
    public FunderAssessment assess(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder) {

        List<RejectionReason> reasons = new ArrayList<>();

        for (FunderRule rule : rules) {
            Optional<RejectionReason> rejectionReason = rule.evaluate(application, mortgageProduct, funder);

            if (rejectionReason.isPresent()) {
                reasons.add(rejectionReason.get());
            }
        }

        FunderAssessment assessment = new FunderAssessment();
        assessment.setFunderId(funder.getFunderId());
        assessment.setEligibleFunder(reasons.isEmpty());
        assessment.setRejectionReasons(reasons);

        return assessment;
    }
}
