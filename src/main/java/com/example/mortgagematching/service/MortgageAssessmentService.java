package com.example.mortgagematching.service;

import com.example.mortgagematching.model.AssessmentResult;
import com.example.mortgagematching.model.MortgageApplication;

public interface MortgageAssessmentService {

    AssessmentResult assess(MortgageApplication application);
}

