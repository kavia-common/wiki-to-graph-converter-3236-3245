# Wiki to Graph Backend

Run
- ./gradlew bootRun
- Swagger UI: http://localhost:3001/swagger-ui.html
- Health: http://localhost:3001/health

API
- POST /api/convert
  - Body: { "url": "http://...", "content": "raw wiki text" } (either url or content required)
  - Response: { "jobId": "uuid", "status": "accepted" }
- GET /api/graph/{jobId}
  - 200 OK with GraphResult when done
  - 102 Processing if still processing
  - 404 if job not found
  - 500 if job errored

CORS
- Allowed origins (for local dev):
  - http://localhost:3000 (Flutter web default)
  - http://localhost:3001 (backend itself)
  - http://127.0.0.1:3000
  - http://127.0.0.1:3001
- If you serve the frontend on a different port (e.g., 8080, 5173), add that origin to CorsConfig.

Notes
- server.port=3001
- Minimal parser extracts headings (== H ==) as nodes and [[Link]] as relationships

Troubleshooting
- If the browser reports a CORS error when using Flutter web, ensure your web origin is present in CorsConfig.java and restart the backend.
- If port 3001 is busy, either stop the other service or change server.port in src/main/resources/application.properties and update the frontend API_BASE_URL accordingly.
- Verify health endpoint at http://localhost:3001/health returns OK.
