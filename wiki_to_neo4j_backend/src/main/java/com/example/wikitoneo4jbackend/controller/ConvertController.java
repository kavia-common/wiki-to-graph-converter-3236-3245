package com.example.wikitoneo4jbackend.controller;

import com.example.wikitoneo4jbackend.dto.ConvertRequest;
import com.example.wikitoneo4jbackend.dto.ConvertResponse;
import com.example.wikitoneo4jbackend.dto.GraphResult;
import com.example.wikitoneo4jbackend.service.ConversionService;
import com.example.wikitoneo4jbackend.service.JobStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PUBLIC_INTERFACE
 * REST controller providing endpoints to start a conversion job and retrieve results.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Conversion", description = "Endpoints for converting wiki content to a graph model")
public class ConvertController {

    private final ConversionService conversionService;
    private final JobStore jobStore;

    public ConvertController(ConversionService conversionService, JobStore jobStore) {
        this.conversionService = conversionService;
        this.jobStore = jobStore;
    }

    // PUBLIC_INTERFACE
    @PostMapping("/convert")
    @Operation(
            summary = "Start conversion",
            description = "Accepts either a URL to fetch wiki content or raw content. Returns a jobId immediately. " +
                    "Processing is performed synchronously for demo purposes, so the result is typically available quickly.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ConvertRequest.class))
            )
    )
    public ResponseEntity<ConvertResponse> convert(@Valid @RequestBody ConvertRequest request) {
        if ((request.getUrl() == null || request.getUrl().isBlank())
                && (request.getContent() == null || request.getContent().isBlank())) {
            return ResponseEntity.badRequest().body(new ConvertResponse(null, "Either url or content must be provided"));
        }

        String jobId = conversionService.startConversion(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ConvertResponse(jobId, "accepted"));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/graph/{jobId}")
    @Operation(
            summary = "Get graph result",
            description = "Retrieves the graph result for a given jobId, if available."
    )
    public ResponseEntity<?> getGraph(@PathVariable @NotBlank String jobId) {
        JobStore.Job job = jobStore.getJob(jobId);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }
        if ("error".equals(job.status())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(job.errorMessage());
        }
        GraphResult result = job.result();
        if (result == null || !"done".equals(job.status())) {
            return ResponseEntity.status(HttpStatus.PROCESSING).body("processing");
        }
        return ResponseEntity.ok(result);
    }
}
