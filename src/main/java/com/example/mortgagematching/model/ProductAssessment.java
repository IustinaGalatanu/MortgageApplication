package com.example.mortgagematching.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "productId",
        "eligibleProduct",
        "rejectionReasons",
        "funderAssessments"
})
public class ProductAssessment {

    private String productId;
    private boolean eligibleProduct;
    private List<RejectionReason> rejectionReasons;
    private List<FunderAssessment> funderAssessments;
}

