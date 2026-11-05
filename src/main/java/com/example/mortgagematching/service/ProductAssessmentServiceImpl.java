package com.example.mortgagematching.service;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.ProductAssesment;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.service.rule.ProductRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductAssessmentServiceImpl implements ProductAssessmentService {

    List<ProductRule> rules;

    public ProductAssessmentServiceImpl(List<ProductRule> rule) {
        this.rules = rule;
    }

    @Override
    public ProductAssesment assess(MortgageApplication application, MortgageProduct mortgageProduct) {

        List<RejectionReason> reasons = new ArrayList<>();

        for (ProductRule rule : rules) {
            Optional<RejectionReason> rejectionReason = rule.evaluate(application, mortgageProduct);

            if (rejectionReason.isPresent()) {
                reasons.add(rejectionReason.get());
            }
        }

        ProductAssesment assesment = new ProductAssesment();
        assesment.setProductId(mortgageProduct.getProductId());
        assesment.setEligibleProduct(reasons.isEmpty());
        assesment.setRejectionReasons(reasons);
        assesment.setFunderAssesments(List.of());

        return assesment;
    }
}
