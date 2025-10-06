# wiki-to-graph-converter-3236-3245

Backend (Spring Boot) provides REST API to convert wiki text or a URL into a graph structure.

How to run
- From the backend root:
  - cd wiki_to_neo4j_backend
  - ./gradlew bootRun
- Swagger UI: http://localhost:3001/swagger-ui.html
- Health: http://localhost:3001/health

API
- POST /api/convert
  - Body: { "url": "http://...", "content": "raw wiki text" } (either url or content required)
  - Returns: { "jobId": "uuid", "status": "accepted" }
- GET /api/graph/{jobId}
  - Returns:
    - 200 with GraphResult:
      {
        "nodes":[{"id":"...", "label":"Topic", "properties":{"name":"Heading"}}],
        "relationships":[{"id":"...", "source":"nodeId", "target":"nodeId", "type":"MENTIONS", "properties":{"context":"heading"}}],
        "cypher":"// Generated Cypher ..."
      }
    - 102 Processing if still processing
    - 404 if job not found
    - 500 if job errored

Notes
- CORS enabled for localhost:3000/3001 and 127.0.0.1 variants. If your frontend runs on another port (e.g., 8080, 5173), add it to CorsConfig and restart.
- Server listens on port 3001.
- Minimal parser: headings (== H ==) become nodes, [[Link]] tokens become relationships.

Quick start (full stack)
- Start backend: `cd wiki_to_neo4j_backend && ./gradlew bootRun`
- Start frontend: see ../wiki-to-graph-converter-3236-3246/wiki_to_neo4j_frontend/README.md
- Ensure the frontend .env has `API_BASE_URL=http://localhost:3001` or leave default.

Troubleshooting
- CORS issues from web: confirm your web origin is listed in src/main/java/.../config/CorsConfig.java.
- Port conflicts on 3001: change server.port in application.properties and update frontend API_BASE_URL.
- Verify liveness via http://localhost:3001/health.