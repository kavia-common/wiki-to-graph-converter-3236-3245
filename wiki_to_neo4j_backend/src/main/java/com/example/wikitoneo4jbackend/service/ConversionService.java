package com.example.wikitoneo4jbackend.service;

import com.example.wikitoneo4jbackend.dto.ConvertRequest;
import com.example.wikitoneo4jbackend.dto.GraphResult;
import com.example.wikitoneo4jbackend.util.WikiParser;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

/**
 * PUBLIC_INTERFACE
 * Service responsible for receiving conversion requests, fetching content if needed,
 * parsing it into a graph, and storing the result in JobStore.
 */
@Service
public class ConversionService {

    private final JobStore jobStore;
    private final WikiParser wikiParser;
    private final RestClient restClient;

    public ConversionService(JobStore jobStore) {
        this.jobStore = jobStore;
        this.wikiParser = new WikiParser();
        // RestClient backed by JDK HttpClient for simple fetch
        this.restClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory())
                .build();
    }

    // PUBLIC_INTERFACE
    /** Starts a conversion job synchronously for demo; returns the jobId immediately. */
    public String startConversion(ConvertRequest request) {
        String jobId = jobStore.createJob();
        try {
            String content = resolveContent(request);
            GraphResult result = wikiParser.parse(content);
            jobStore.completeJob(jobId, result);
        } catch (Exception ex) {
            jobStore.errorJob(jobId, "Conversion error: " + ex.getMessage());
        }
        return jobId;
    }

    private String resolveContent(ConvertRequest request) {
        if (request.getContent() != null && !request.getContent().isBlank()) {
            return request.getContent();
        }
        if (request.getUrl() != null && !request.getUrl().isBlank()) {
            // Basic fetch with RestClient
            URI uri = URI.create(request.getUrl());
            return restClient.get()
                    .uri(uri)
                    .accept(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.ALL)
                    .retrieve()
                    .body(String.class);
        }
        throw new IllegalArgumentException("Either url or content must be provided");
    }
}
