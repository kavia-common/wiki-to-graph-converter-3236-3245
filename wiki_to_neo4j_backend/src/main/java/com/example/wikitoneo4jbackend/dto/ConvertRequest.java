package com.example.wikitoneo4jbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PUBLIC_INTERFACE
 * Request payload for conversion.
 * Provide either a URL to fetch content from or raw content directly.
 */
public class ConvertRequest {

    @Schema(description = "URL to fetch wiki content from", example = "https://en.wikipedia.org/wiki/Graph_database")
    private String url;

    @Schema(description = "Raw wiki content", example = "== Heading 1 ==\nSome content with [[LinkA]] to other pages.")
    private String content;

    public ConvertRequest() {}

    public ConvertRequest(String url, String content) {
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
