package com.example.mortgagematching.controller;

import com.example.mortgagematching.dto.MortgageApplicationRequest;
import com.example.mortgagematching.mapper.MortgageApplicationMapper;
import com.example.mortgagematching.model.AssessmentResult;
import com.example.mortgagematching.model.FunderAssessment;
import com.example.mortgagematching.model.MortgageApplication;
import com.example.mortgagematching.model.ProductAssessment;
import com.example.mortgagematching.model.RejectionReason;
import com.example.mortgagematching.model.enums.ApplicantType;
import com.example.mortgagematching.model.enums.PropertyLocation;
import com.example.mortgagematching.model.enums.PropertyType;
import com.example.mortgagematching.model.enums.RejectionType;
import com.example.mortgagematching.model.enums.RepaymentType;
import com.example.mortgagematching.service.MortgageAssessmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MortgageControllerTest {

    private final MortgageAssessmentService service = mock(MortgageAssessmentService.class);
    private final MortgageController controller = new MortgageController(service, new MortgageApplicationMapper());

    @Test
    void shouldReturnEligibleProductsWithFundersAndIneligibleProducts() {
        AssessmentResult expected = new AssessmentResult();
        expected.setCaseReference("CASE-1");
        expected.setProductAssessments(List.of(
                productNotEligible(),
                productWithMatchingFunder(),
                productWithoutMatchingFunder()));

        when(service.assess(any(MortgageApplication.class))).thenReturn(expected);

        AssessmentResult result = controller.assess(buildRequest());

        verify(service).assess(any(MortgageApplication.class));
        assertEquals("CASE-1", result.getCaseReference());
        assertEquals(3, result.getProductAssessments().size());
        assertProductNotEligible(result.getProductAssessments().get(0));
        assertProductWithMatchingFunder(result.getProductAssessments().get(1));
        assertProductWithoutMatchingFunder(result.getProductAssessments().get(2));
    }

    private ProductAssessment productNotEligible() {
        return productAssessment("HMO_5Y_75", false,
                List.of(rejection(RejectionType.PROPERTY_TYPE_NOT_SUPPORTED)),
                List.of());
    }

    private ProductAssessment productWithMatchingFunder() {
        return productAssessment("STANDARD_2Y_75", true, List.of(), List.of(
                funderAssessment("FUNDER_A", true, List.of()),
                funderAssessment("FUNDER_B", false, List.of(rejection(RejectionType.LTV_EXCEEDED)))));
    }

    private ProductAssessment productWithoutMatchingFunder() {
        return productAssessment("STANDARD_5Y_75", true, List.of(), List.of(
                funderAssessment("FUNDER_A", false, List.of(rejection(RejectionType.LTV_EXCEEDED))),
                funderAssessment("FUNDER_B", false, List.of(rejection(RejectionType.PRODUCT_NOT_SUPPORTED)))));
    }

    private void assertProductNotEligible(ProductAssessment product) {
        assertEquals("HMO_5Y_75", product.getProductId());
        assertFalse(product.isEligibleProduct());
        assertEquals(RejectionType.PROPERTY_TYPE_NOT_SUPPORTED,
                product.getRejectionReasons().get(0).getRejectionType());
        assertTrue(product.getFunderAssessments().isEmpty());
    }

    private void assertProductWithMatchingFunder(ProductAssessment product) {
        assertEquals("STANDARD_2Y_75", product.getProductId());
        assertTrue(product.isEligibleProduct());
        assertEquals(2, product.getFunderAssessments().size());
        assertEquals("FUNDER_A", product.getFunderAssessments().get(0).getFunderId());
        assertTrue(product.getFunderAssessments().get(0).isEligibleFunder());
        assertFalse(product.getFunderAssessments().get(1).isEligibleFunder());
    }

    private void assertProductWithoutMatchingFunder(ProductAssessment product) {
        assertEquals("STANDARD_5Y_75", product.getProductId());
        assertTrue(product.isEligibleProduct());
        assertEquals(2, product.getFunderAssessments().size());
        assertFalse(product.getFunderAssessments().get(0).isEligibleFunder());
        assertFalse(product.getFunderAssessments().get(1).isEligibleFunder());
    }

    private MortgageApplicationRequest buildRequest() {
        MortgageApplicationRequest request = new MortgageApplicationRequest();
        request.setCaseReference("CASE-1");
        request.setLoanAmount(new BigDecimal("150000"));
        request.setPropertyValue(new BigDecimal("250000"));
        request.setExpectedMonthlyRent(new BigDecimal("2000"));
        request.setPropertyType(PropertyType.STANDARD);
        request.setApplicantType(ApplicantType.INDIVIDUAL);
        request.setPropertyLocation(PropertyLocation.ENGLAND);
        request.setHasLandlordExperience(true);
        request.setRepaymentType(RepaymentType.INTEREST_ONLY);
        return request;
    }

    private ProductAssessment productAssessment(String productId, boolean eligible,
            List<RejectionReason> reasons,
            List<FunderAssessment> funders) {
        ProductAssessment assessment = new ProductAssessment();
        assessment.setProductId(productId);
        assessment.setEligibleProduct(eligible);
        assessment.setRejectionReasons(reasons);
        assessment.setFunderAssessments(funders);
        return assessment;
    }

    private FunderAssessment funderAssessment(String funderId, boolean eligible, List<RejectionReason> reasons) {
        FunderAssessment assessment = new FunderAssessment();
        assessment.setFunderId(funderId);
        assessment.setEligibleFunder(eligible);
        assessment.setRejectionReasons(reasons);
        return assessment;
    }

    private RejectionReason rejection(RejectionType type) {
        RejectionReason reason = new RejectionReason();
        reason.setRejectionType(type);
        reason.setMessage(type.name());
        return reason;
    }
}
