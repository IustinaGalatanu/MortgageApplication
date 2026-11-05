package com.example.mortgagematching.model;

import com.example.mortgagematching.model.enums.RejectionType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "rejectionType",
        "message"
})
public class RejectionReason {

    private RejectionType rejectionType;
    private String message;
}
