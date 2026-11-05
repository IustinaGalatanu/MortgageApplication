package com.example.mortgagematching.service;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.ProductAssessment;


public interface ProductAssessmentService {

    ProductAssessment assess(MortgageApplication application, MortgageProduct mortgageProduct);
}
