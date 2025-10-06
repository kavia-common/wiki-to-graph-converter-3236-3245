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
- Allowed origins:
  - http://localhost:3000, http://localhost:3001, http://127.0.0.1:3000, http://127.0.0.1:3001

Notes
- server.port=3001
- Minimal parser extracts headings (== H ==) as nodes and [[Link]] as relationships
