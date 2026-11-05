package com.example.mortgagematching.controller;

import com.example.mortgagematching.dto.MortgageApplicationRequest;
import com.example.mortgagematching.mapper.MortgageApplicationMapper;
import com.example.mortgagematching.model.AssessmentResult;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.service.MortgageAssessmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mortgage-assessments")
public class MortgageController {

    private final MortgageAssessmentService mortgageAssessmentService;
    private final MortgageApplicationMapper mapper;

    public MortgageController(MortgageAssessmentService mortgageAssessmentService, MortgageApplicationMapper mapper) {
        this.mortgageAssessmentService = mortgageAssessmentService;
        this.mapper = mapper;
    }

    @PostMapping
    public AssessmentResult assess(@Valid @RequestBody MortgageApplicationRequest mortgageApplicationRequest) {

        MortgageApplication application = mapper.toApplication(mortgageApplicationRequest);
        return mortgageAssessmentService.assess(application);
    }
}
