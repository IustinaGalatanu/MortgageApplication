package com.example.mortgagematching.service;

import com.example.mortgagematching.catalog.FunderCatalog;
import com.example.mortgagematching.catalog.ProductCatalog;
import com.example.mortgagematching.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MortgageAssessmentServiceImpl implements MortgageAssesmentService {

    private final ProductCatalog productCatalog;
    private final FunderCatalog funderCatalog;
    private final ProductAssessmentServiceImpl productAssessmentService;
    private final FunderAssessmentServiceImpl funderAssessmentService;

    public MortgageAssessmentServiceImpl(ProductCatalog productCatalog, FunderCatalog funderCatalog, ProductAssessmentServiceImpl productAssessmentService, FunderAssessmentServiceImpl funderAssessmentService) {
        this.productCatalog = productCatalog;
        this.funderCatalog = funderCatalog;
        this.productAssessmentService = productAssessmentService;
        this.funderAssessmentService = funderAssessmentService;
    }

    @Override
    public AssessmentResult assess(MortgageApplication application) {

        List<ProductAssesment> productAssessments = new ArrayList<>();

        for (MortgageProduct product : productCatalog.getProductList()) {
            ProductAssesment productAssesment = productAssessmentService.assess(application, product);

            if (productAssesment.isEligibleProduct()) {
                List<FunderAssesment> funderAssessments = new ArrayList<>();

                for (Funder funder : funderCatalog.getFunderList()) {
                    FunderAssesment funderAssesment = funderAssessmentService.assess(application, product, funder);
                    funderAssessments.add(funderAssesment);

                }

                funderAssessments.sort(Comparator.comparing(FunderAssesment::getFunderId));
                productAssesment.setFunderAssesments(funderAssessments);
            }
            productAssessments.add(productAssesment);
        }

        productAssessments.sort(Comparator.comparing(ProductAssesment::getProductId));

        AssessmentResult result = new AssessmentResult();
        result.setCaseReference(application.getCaseReference());
        result.setProductAssesments(productAssessments);

        return result;
    }
}
