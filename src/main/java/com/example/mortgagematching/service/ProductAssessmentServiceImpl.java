package com.example.mortgagematching.service;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.ProductAssessment;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductAssessmentServiceImpl implements ProductAssessmentService {

    List<ProductRule> rules;

    public ProductAssessmentServiceImpl(List<ProductRule> rules) {
        this.rules = rules;
    }

    @Override
    public ProductAssessment assess(MortgageApplication application, MortgageProduct mortgageProduct) {

        List<RejectionReason> reasons = new ArrayList<>();

        for (ProductRule rule : rules) {
            Optional<RejectionReason> rejectionReason = rule.evaluate(application, mortgageProduct);

            if (rejectionReason.isPresent()) {
                reasons.add(rejectionReason.get());
            }
        }

        ProductAssessment assessment = new ProductAssessment();
        assessment.setProductId(mortgageProduct.getProductId());
        assessment.setEligibleProduct(reasons.isEmpty());
        assessment.setRejectionReasons(reasons);
        assessment.setFunderAssessments(List.of());

        return assessment;
    }
}
