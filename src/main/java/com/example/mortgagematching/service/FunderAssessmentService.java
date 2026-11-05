package com.example.mortgagematching.service;

import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.FunderAssesment;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;

public interface FunderAssessmentService {

    FunderAssesment assess(MortgageApplication application, MortgageProduct mortgageProduct, Funder funder);
}
