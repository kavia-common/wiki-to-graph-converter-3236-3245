package com.example.wikitoneo4jbackend.util;

import com.example.wikitoneo4jbackend.dto.GraphResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PUBLIC_INTERFACE
 * Minimal parser extracting headings as nodes and link-like tokens ([[Link]]) as relationships.
 * Ensures node uniqueness by name (label property).
 */
public class WikiParser {

    private static final Pattern HEADING_PATTERN = Pattern.compile("^\\s*={2,6}\\s*(.+?)\\s*={2,6}\\s*$");
    private static final Pattern LINK_PATTERN = Pattern.compile("\\[\\[(.+?)]\\]");

    // PUBLIC_INTERFACE
    /** Parses the provided wiki content into a graph structure. */
    public GraphResult parse(String content) {
        if (content == null) {
            return new GraphResult(Collections.emptyList(), Collections.emptyList(), "");
        }

        Map<String, GraphResult.Node> nodeByName = new LinkedHashMap<>();
        List<GraphResult.Relationship> rels = new ArrayList<>();

        String currentHeading = null;
        String[] lines = content.split("\\R");

        for (String line : lines) {
            Matcher h = HEADING_PATTERN.matcher(line);
            if (h.matches()) {
                currentHeading = h.group(1).trim();
                ensureNode(nodeByName, currentHeading);
                continue;
            }
            Matcher m = LINK_PATTERN.matcher(line);
            while (m.find()) {
                String link = m.group(1).trim();
                if (link.isEmpty()) continue;
                ensureNode(nodeByName, link);
                // If under a heading, connect heading -> link
                String source = currentHeading != null ? currentHeading : "Document";
                ensureNode(nodeByName, source);

                String relId = UUID.randomUUID().toString();
                GraphResult.Relationship r = new GraphResult.Relationship(
                        relId,
                        nodeByName.get(source).getId(),
                        nodeByName.get(link).getId(),
                        "MENTIONS",
                        Map.of("context", currentHeading != null ? "heading" : "body")
                );
                rels.add(r);
            }
        }

        // Build node list maintaining insertion order
        List<GraphResult.Node> nodes = new ArrayList<>(nodeByName.values());

        // Optionally create a simple Cypher
        String cypher = buildCypher(nodes, rels);

        return new GraphResult(nodes, rels, cypher);
    }

    private void ensureNode(Map<String, GraphResult.Node> nodeByName, String name) {
        nodeByName.computeIfAbsent(name, n -> {
            Map<String, Object> props = new LinkedHashMap<>();
            props.put("name", n);
            return new GraphResult.Node(UUID.randomUUID().toString(), "Topic", props);
        });
    }

    private String buildCypher(List<GraphResult.Node> nodes, List<GraphResult.Relationship> rels) {
        StringBuilder sb = new StringBuilder();
        sb.append("// Generated Cypher\n");
        for (GraphResult.Node n : nodes) {
            String name = String.valueOf(n.getProperties().get("name")).replace("'", "\\'");
            sb.append("MERGE (n:").append(n.getLabel()).append(" {id:'").append(n.getId()).append("'}) ")
              .append("SET n.name='").append(name).append("';\n");
        }
        for (GraphResult.Relationship r : rels) {
            sb.append("MATCH (a {id:'").append(r.getSource()).append("'}), (b {id:'")
              .append(r.getTarget()).append("'}) ")
              .append("MERGE (a)-[:").append(r.getType()).append("]->(b);\n");
        }
        return sb.toString();
    }
}
