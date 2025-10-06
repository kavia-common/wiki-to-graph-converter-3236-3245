package com.example.wikitoneo4jbackend.service;

import com.example.wikitoneo4jbackend.dto.GraphResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PUBLIC_INTERFACE
 * In-memory store for conversion jobs with status and results.
 */
@Component
public class JobStore {

    public record Job(String id, String status, GraphResult result, String errorMessage) {}

    private final Map<String, Job> store = new ConcurrentHashMap<>();

    // PUBLIC_INTERFACE
    /** Creates a new job with status 'processing' and returns its ID. */
    public String createJob() {
        String id = UUID.randomUUID().toString();
        store.put(id, new Job(id, "processing", null, null));
        return id;
    }

    // PUBLIC_INTERFACE
    /** Marks the job as done and sets the result. */
    public void completeJob(String id, GraphResult result) {
        store.computeIfPresent(id, (k, v) -> new Job(id, "done", result, null));
    }

    // PUBLIC_INTERFACE
    /** Marks the job as error with a message. */
    public void errorJob(String id, String error) {
        store.computeIfPresent(id, (k, v) -> new Job(id, "error", null, error));
    }

    // PUBLIC_INTERFACE
    /** Retrieves a job by ID. */
    public Job getJob(String id) {
        return store.get(id);
    }
}
