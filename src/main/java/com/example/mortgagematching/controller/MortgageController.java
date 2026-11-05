package com.example.mortgagematching.controller;

import com.example.mortgagematching.dto.MortgageApplicationRequest;
import com.example.mortgagematching.mapper.MortgageApplicationMapper;
import com.example.mortgagematching.model.AssessmentResult;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.service.MortgageAssesmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mortgage-assessments")
public class MortgageController {

    private final MortgageAssesmentService mortgageAssesmentService;
    private final MortgageApplicationMapper mapper;

    public MortgageController(MortgageAssesmentService mortgageAssesmentService, MortgageApplicationMapper mapper) {
        this.mortgageAssesmentService = mortgageAssesmentService;
        this.mapper = mapper;
    }

    @PostMapping
    public AssessmentResult assess(@Valid @RequestBody MortgageApplicationRequest mortgageApplicationRequest) {

        MortgageApplication application = mapper.toApplication(mortgageApplicationRequest);
        return mortgageAssesmentService.assess(application);
    }
}
