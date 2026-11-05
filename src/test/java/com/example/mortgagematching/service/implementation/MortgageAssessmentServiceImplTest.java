
package com.example.mortgagematching.service.implementation;

import com.example.mortgagematching.catalog.FunderCatalog;
import com.example.mortgagematching.catalog.ProductCatalog;
import com.example.mortgagematching.model.AssessmentResult;
import com.example.mortgagematching.model.Funder;
import com.example.mortgagematching.model.FunderAssessment;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.MortgageProduct;
import com.example.mortgagematching.model.ProductAssessment;
import com.example.mortgagematching.service.FunderAssessmentService;
import com.example.mortgagematching.service.MortgageAssessmentServiceImpl;
import com.example.mortgagematching.service.ProductAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MortgageAssessmentServiceImplTest {

    private ProductCatalog productCatalog;
    private FunderCatalog funderCatalog;
    private ProductAssessmentService productAssessmentService;
    private FunderAssessmentService funderAssessmentService;
    private MortgageAssessmentServiceImpl service;
    private MortgageApplication application;

    @BeforeEach
    void setUp() {
        productCatalog = mock(ProductCatalog.class);
        funderCatalog = mock(FunderCatalog.class);
        productAssessmentService = mock(ProductAssessmentService.class);
        funderAssessmentService = mock(FunderAssessmentService.class);

        service = new MortgageAssessmentServiceImpl(
                productCatalog,
                funderCatalog,
                productAssessmentService,
                funderAssessmentService
        );

        application = new MortgageApplication();
        application.setCaseReference("CASE-123");
    }

    @Test
    void shouldReturnEmptyAssessmentsWhenNoProductsExist() {
        when(productCatalog.getProductList()).thenReturn(List.of());

        AssessmentResult result = service.assess(application);

        assertEquals("CASE-123", result.getCaseReference());
        assertTrue(result.getProductAssessments().isEmpty());

        verifyNoInteractions(productAssessmentService);
        verifyNoInteractions(funderAssessmentService);
    }

    @Test
    void shouldReturnProductsSortedByProductId() {
        MortgageProduct productB = new MortgageProduct();
        productB.setProductId("STANDARD_5Y_75");

        MortgageProduct productA = new MortgageProduct();
        productA.setProductId("HMO_5Y_75");

        ProductAssessment assessmentB =
                createProductAssessment("STANDARD_5Y_75", false);

        ProductAssessment assessmentA =
                createProductAssessment("HMO_5Y_75", false);

        when(productCatalog.getProductList())
                .thenReturn(List.of(productB, productA));

        when(productAssessmentService.assess(application, productB))
                .thenReturn(assessmentB);

        when(productAssessmentService.assess(application, productA))
                .thenReturn(assessmentA);

        AssessmentResult result = service.assess(application);

        assertEquals(2, result.getProductAssessments().size());

        assertEquals("HMO_5Y_75",
                result.getProductAssessments().get(0).getProductId());

        assertEquals("STANDARD_5Y_75",
                result.getProductAssessments().get(1).getProductId());

        verifyNoInteractions(funderAssessmentService);
    }

    @Test
    void shouldAssessFundersOnlyForEligibleProducts() {
        MortgageProduct eligibleProduct = new MortgageProduct();
        eligibleProduct.setProductId("STANDARD_2Y_75");

        MortgageProduct ineligibleProduct = new MortgageProduct();
        ineligibleProduct.setProductId("HMO_5Y_75");

        Funder funder = new Funder();
        funder.setFunderId("FUNDER_A");

        ProductAssessment eligibleAssessment =
                createProductAssessment("STANDARD_2Y_75", true);

        ProductAssessment ineligibleAssessment =
                createProductAssessment("HMO_5Y_75", false);

        FunderAssessment funderAssessment =
                createFunderAssessment("FUNDER_A", true);

        when(productCatalog.getProductList())
                .thenReturn(List.of(eligibleProduct, ineligibleProduct));

        when(funderCatalog.getFunderList())
                .thenReturn(List.of(funder));

        when(productAssessmentService.assess(application, eligibleProduct))
                .thenReturn(eligibleAssessment);

        when(productAssessmentService.assess(application, ineligibleProduct))
                .thenReturn(ineligibleAssessment);

        when(funderAssessmentService.assess(application, eligibleProduct, funder))
                .thenReturn(funderAssessment);

        AssessmentResult result = service.assess(application);

        assertEquals(2, result.getProductAssessments().size());
        assertEquals(1, eligibleAssessment.getFunderAssessments().size());
        assertTrue(ineligibleAssessment.getFunderAssessments().isEmpty());

        verify(funderAssessmentService)
                .assess(application, eligibleProduct, funder);

        verify(funderAssessmentService, never())
                .assess(application, ineligibleProduct, funder);
    }

    @Test
    void shouldReturnFundersSortedByFunderId() {
        MortgageProduct product = new MortgageProduct();
        product.setProductId("STANDARD_2Y_75");

        Funder funderB = new Funder();
        funderB.setFunderId("FUNDER_B");

        Funder funderA = new Funder();
        funderA.setFunderId("FUNDER_A");

        ProductAssessment productAssessment =
                createProductAssessment("STANDARD_2Y_75", true);

        FunderAssessment assessmentB =
                createFunderAssessment("FUNDER_B", true);

        FunderAssessment assessmentA =
                createFunderAssessment("FUNDER_A", false);

        when(productCatalog.getProductList())
                .thenReturn(List.of(product));

        when(funderCatalog.getFunderList())
                .thenReturn(List.of(funderB, funderA));

        when(productAssessmentService.assess(application, product))
                .thenReturn(productAssessment);

        when(funderAssessmentService.assess(application, product, funderB))
                .thenReturn(assessmentB);

        when(funderAssessmentService.assess(application, product, funderA))
                .thenReturn(assessmentA);

        AssessmentResult result = service.assess(application);

        List<FunderAssessment> funders = result.getProductAssessments()
                .get(0).getFunderAssessments();

        assertEquals(2, funders.size());
        assertEquals("FUNDER_A", funders.get(0).getFunderId());
        assertEquals("FUNDER_B", funders.get(1).getFunderId());
    }

    private ProductAssessment createProductAssessment(
            String productId, boolean eligible) {

        ProductAssessment assessment = new ProductAssessment();
        assessment.setProductId(productId);
        assessment.setEligibleProduct(eligible);
        assessment.setRejectionReasons(List.of());
        assessment.setFunderAssessments(List.of());

        return assessment;
    }

    private FunderAssessment createFunderAssessment(
            String funderId, boolean eligible) {

        FunderAssessment assessment = new FunderAssessment();
        assessment.setFunderId(funderId);
        assessment.setEligibleFunder(eligible);
        assessment.setRejectionReasons(List.of());

        return assessment;
    }
}
