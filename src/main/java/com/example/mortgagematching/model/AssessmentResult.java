package com.example.mortgagematching.model;

import lombok.Data;

import java.util.List;

@Data
public class AssessmentResult {

    private String caseReference;
    private List<ProductAssesment> productAssesments;

}
