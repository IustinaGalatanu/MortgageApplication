package com.example.mortgagematching.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "funderId",
        "eligibleFunder",
        "rejectionReasons"
})
public class FunderAssesment {

    private String funderId;
    private boolean eligibleFunder;
    private List<RejectionReason> rejectionReasons;
}
