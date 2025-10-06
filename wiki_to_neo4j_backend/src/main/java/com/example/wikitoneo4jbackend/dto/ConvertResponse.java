package com.example.wikitoneo4jbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PUBLIC_INTERFACE
 * Response when a conversion request is accepted.
 */
public class ConvertResponse {

    @Schema(description = "Identifier of the submitted job", example = "b3a3a4b0-f2f6-4b8d-9a1e-1234567890ab")
    private String jobId;

    @Schema(description = "Status message", example = "accepted")
    private String status;

    public ConvertResponse() {}

    public ConvertResponse(String jobId, String status) {
        this.jobId = jobId;
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
