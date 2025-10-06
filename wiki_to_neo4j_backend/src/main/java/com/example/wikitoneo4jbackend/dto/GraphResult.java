package com.example.wikitoneo4jbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * PUBLIC_INTERFACE
 * Represents the output graph consisting of nodes and relationships, with an optional Cypher script.
 */
public class GraphResult {

    @Schema(description = "List of nodes in the graph")
    private List<Node> nodes = new ArrayList<>();

    @Schema(description = "List of relationships in the graph")
    private List<Relationship> relationships = new ArrayList<>();

    @Schema(description = "Optional Cypher to create the graph in Neo4j")
    private String cypher;

    public GraphResult() {}

    public GraphResult(List<Node> nodes, List<Relationship> relationships, String cypher) {
        this.nodes = nodes != null ? nodes : new ArrayList<>();
        this.relationships = relationships != null ? relationships : new ArrayList<>();
        this.cypher = cypher;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public String getCypher() {
        return cypher;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    public void setCypher(String cypher) {
        this.cypher = cypher;
    }

    // PUBLIC_INTERFACE
    /** Node DTO representing a graph node. Ensures uniqueness by label/name externally. */
    public static class Node {
        private String id;
        private String label;
        private Map<String, Object> properties = new LinkedHashMap<>();

        public Node() {}

        public Node(String id, String label, Map<String, Object> properties) {
            this.id = id;
            this.label = label;
            if (properties != null) {
                this.properties = properties;
            }
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }

    // PUBLIC_INTERFACE
    /** Relationship DTO representing a directed edge between nodes. */
    public static class Relationship {
        private String id;
        private String source;
        private String target;
        private String type;
        private Map<String, Object> properties = new LinkedHashMap<>();

        public Relationship() {}

        public Relationship(String id, String source, String target, String type, Map<String, Object> properties) {
            this.id = id;
            this.source = source;
            this.target = target;
            this.type = type;
            if (properties != null) {
                this.properties = properties;
            }
        }

        public String getId() {
            return id;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

        public String getType() {
            return type;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }
}
