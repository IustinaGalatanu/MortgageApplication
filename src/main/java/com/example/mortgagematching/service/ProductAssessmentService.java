package com.example.mortgagematching.service;

import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.ProductAssesment;


public interface ProductAssessmentService {

    ProductAssesment assess(MortgageApplication application, MortgageProduct mortgageProduct);
}
