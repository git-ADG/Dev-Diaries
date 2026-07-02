# Dev Diaries (avaiable at : https://dev-diaries-1-c0q7.onrender.com)

A secure, multi-tenant knowledge management system and smart document tagging engine engineered custom for software developers.

Dev Diaries behaves like an IDE right inside your web browser, allowing developers to cleanly log terminal scripts, capture code blocks with native syntax highlighting, and compile markdown documentation. It is architected as a full-stack Monorepo pairing an enterprise Spring Boot backend with a highly interactive, responsive React Single Page Application (SPA).

---

## Architecture & Core Infrastructure

The application leverages a stateless client-server model optimized for cloud efficiency and zero-downtime execution.

* **Frontend Client:** Compiled using Vite and deployed to a global Content Delivery Network (CDN) via Vercel.
* **Backend API Service:** Containerized with Docker and hosted on Koyeb (utilizing an un-sleeping instance to guarantee instantaneous API availability).
* **Persistent Database Layer:** Managed on a cloud PostgreSQL cluster utilizing connection pooling optimized with a `prepareThreshold=0` flag to comfortably handle dynamic transactional connections.

---

## The Tech Stack

### Backend (Enterprise Java Core)
* **Core Engine:** Java 17, Spring Boot 3.x (Spring MVC)
* **Security Configuration:** Spring Security 6 (Stateless JWT Filter Chains, Cryptographic Password Hashing via `BCrypt`)
* **Data Access Layer:** Spring Data JPA, Hibernate ORM, PostgreSQL Driver
* **Query Filtering Engine:** Criteria API & `JpaSpecificationExecutor` for dynamic parameter aggregation
* **API Verification:** Springdoc OpenAPI 3.0 / Swagger UI (Configured for programmatic Bearer token authorization)

### Frontend (Developer Dashboard)
* **Core Architecture:** React 18, Vite, React Router DOM v6
* **Server State & Caching:** TanStack Query v5 (React Query) for smart cache invalidation and query synchronization
* **Network Client:** Axios (Configured with request/response interceptors to automate JWT token injection and stateless session log-outs)
* **Rich Text Processing:** `@monaco-editor/react` (The underlying tokenization engine powering VS Code), `react-markdown`, and `react-syntax-highlighter`
* **UI System:** Tailwind CSS (Enforcing an automatic dark-mode developer aesthetic), `lucide-react` icons, and `date-fns`

---

## Key Technical Implementations

### Multi-Tenant Data Isolation & Security
To prevent IDOR (Insecure Direct Object Reference) vulnerabilities, data access is decoupled from a single-user architecture into a strict multi-tenant format. Incoming HTTP requests pass through a custom `OncePerRequestFilter` bouncer where the JWT is validated. Row-level data fetching is constrained behind JPA Specifications that dynamically append the authenticated tenant principal's unique identifier to all database transactions.

### Tag Color Hashing
Instead of hardcoding layout colors for user-generated note tags, the client runs a deterministic bitwise hashing algorithm:
```javascript
let hash = 0;
for (let i = 0; i < tagName.length; i++) {
  hash = tagName.charCodeAt(i) + ((hash << 5) - hash);
}
const index = Math.abs(hash) % preDefinedTailwindColorPalette.length;
```

This guarantees that any given tag (e.g., `#Docker`) instantly maps to the exact same pastel/neon visual border and background combination globally across different client screens.

### Dynamic Parameter Predicate Chaining

Searching notes parses keywords, clicked tags, and file formats simultaneously. The backend aggregates these optional filters by utilizing Predicates inside a Spring Data Specification building block, combining active constraints into a single SQL statement sent cleanly to PostgreSQL.

### Collision-Proof Binary ZIP Streaming

The native Java file backup pipeline queries data matching user permissions, applies regular expression character sanitization (`[^a-zA-Z0-9.-]`) to note titles, and appends an 8-character slice of the database `UUID` to filenames. This structural validation step entirely prevents name-collision crashes within Java's `ZipOutputStream` stream buffer when processing duplicate entry names.

---

## Cloud Deployment & Performance Optimizations

```text
               [ React Frontend (Vercel CDN Client) ]
                                 │
                         HTTPS + JWT Bearer
                                 ▼
            [ Spring Boot Backend API (Koyeb Singapore Container) ]
                                 │
                   Low-Latency Local Query Loop (1-5ms)
                                 ▼
         [ Cloud PostgreSQL Database Cluster (ap-southeast Region) ]
```

### Eliminating Cross-Continental Latency Loops

During initial deployment, the full-stack system faced major performance lag due to a geographic distribution split: the frontend was global, the Spring Boot container was running in Oregon (USA), and the PostgreSQL database instance was running in Singapore (`ap-southeast`).

Because Hibernate frequently makes sequential SQL queries to verify relational mappings, every click forced data back-and-forth across the Pacific Ocean multiple times, introducing 500ms+ processing overhead.

* **The Optimization:** Co-located the containerized backend directly into a Koyeb container in the Singapore cluster region. Bringing the business execution logic into the exact same room as the cloud database engine eliminated the cross-ocean detour, dropping raw query overhead down to single-digit milliseconds (1-5ms).

---

## Repository Directory Structure

```text
dev-diaries-monorepo/
├── src/                          # Spring Boot Backend Codebase
│   ├── main/java/com/example/...
│   │   ├── config/               # CORS and Documentation Configurations
│   │   ├── controllers/          # Rest API Endpoints (Auth, Notes)
│   │   ├── dto/                  # Data Transfer Objects & Input Validations
│   │   ├── models/               # Relational Entities (User, Note, Tag)
│   │   ├── security/             # Security filters, JWT Services, App Configs
│   │   ├── services/             # Core Logic (Tagging, Note Management, Exports)
│   │   └── specifications/       # Dynamic JPA SQL Predicate Filters
│   └── main/resources/application.properties
├── frontend/                     # React Vite Frontend Application
│   ├── src/
│   │   ├── components/           # NoteEditor, Reusable Layouts
│   │   ├── context/              # Authentication Context
│   │   ├── pages/                # Login, Dashboard Grids
│   │   ├── services/             # Axios Instance Interceptor Client
│   │   └── utils/                # String Hash Color Generators
│   ├── tailwind.config.js
│   └── vite.config.js
├── Dockerfile                    # Containerization Build Script
└── pom.xml                       # Maven Configuration file
```


## Local Installation & Setup

### Backend Verification

1. Enforce that Java 17 and Maven are installed locally.
2. Verify environment keys matching local database strings inside your .env structure.
3. Boot the API service directly from your root terminal shell:
```bash
mvn spring-boot:run
```


4. Access interactive endpoint models cleanly over: `http://localhost:8080/swagger-ui/index.html`

### Frontend Launch

1. Change directory to the frontend context block:
```bash
cd frontend
npm install
```


2. Start the development hot-reloading server window:
```bash
npm run dev
```


3. Open `http://localhost:5173` to initialize your local developer logs.
