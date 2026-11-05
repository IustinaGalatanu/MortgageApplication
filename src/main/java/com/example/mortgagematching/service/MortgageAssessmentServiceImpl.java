package com.example.mortgagematching.service;

import com.example.mortgagematching.catalog.FunderCatalog;
import com.example.mortgagematching.catalog.ProductCatalog;
import com.example.mortgagematching.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class MortgageAssessmentServiceImpl implements MortgageAssessmentService {

    private final ProductCatalog productCatalog;
    private final FunderCatalog funderCatalog;
    private final ProductAssessmentService productAssessmentService;
    private final FunderAssessmentService funderAssessmentService;

    public MortgageAssessmentServiceImpl(ProductCatalog productCatalog, FunderCatalog funderCatalog,
            ProductAssessmentService productAssessmentService, FunderAssessmentService funderAssessmentService) {
        this.productCatalog = productCatalog;
        this.funderCatalog = funderCatalog;
        this.productAssessmentService = productAssessmentService;
        this.funderAssessmentService = funderAssessmentService;
    }

    @Override
    public AssessmentResult assess(MortgageApplication application) {

        List<ProductAssessment> productAssessments = new ArrayList<>();

        for (MortgageProduct product : productCatalog.getProductList()) {
            ProductAssessment productAssessment = productAssessmentService.assess(application, product);

            if (productAssessment.isEligibleProduct()) {
                List<FunderAssessment> funderAssessments = new ArrayList<>();

                for (Funder funder : funderCatalog.getFunderList()) {
                    FunderAssessment funderAssessment = funderAssessmentService.assess(application, product, funder);
                    funderAssessments.add(funderAssessment);

                }

                funderAssessments.sort(Comparator.comparing(FunderAssessment::getFunderId));
                productAssessment.setFunderAssessments(funderAssessments);
            }
            productAssessments.add(productAssessment);
        }

        productAssessments.sort(Comparator.comparing(ProductAssessment::getProductId));

        AssessmentResult result = new AssessmentResult();
        result.setCaseReference(application.getCaseReference());
        result.setProductAssessments(productAssessments);

        long eligibleProducts = productAssessments.stream()
                .filter(ProductAssessment::isEligibleProduct)
                .count();
        log.info("Assessed caseReference={}: {}/{} products eligible",
                application.getCaseReference(), eligibleProducts, productAssessments.size());

        return result;
    }
}
