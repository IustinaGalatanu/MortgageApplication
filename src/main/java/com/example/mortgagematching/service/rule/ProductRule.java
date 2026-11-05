package com.example.mortgagematching.service.rule;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.RejectionReason;

import java.util.Optional;

public interface ProductRule {

    Optional<RejectionReason> evaluate(
            MortgageApplication application,
            MortgageProduct mortgageProduct
    );
}
