package com.example.mortgagematching.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "productId",
        "eligibleProduct",
        "rejectionReasons",
        "funderAssesments"
})
public class ProductAssesment {

    private String productId;
    private boolean eligibleProduct;
    private List<RejectionReason> rejectionReasons;
    private List<FunderAssesment> funderAssesments;
}
