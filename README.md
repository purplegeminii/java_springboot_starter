# Spring Boot REST API

A production-ready REST API starter built with Spring Boot 3, JPA, Spring Security, and JWT authentication. Structured for developers coming from Django or FastAPI.

---

## Tech Stack

| Layer                 | Technology                     |
|-----------------------|--------------------------------|
| Framework             | Spring Boot 3.5.11             |
| Language              | Java 25                        |
| ORM                   | Spring Data JPA + Hibernate    |
| Database (local)      | H2 (in-memory)                 |
| Database (production) | PostgreSQL                     |
| Authentication        | JWT (jjwt 0.12.5)              |
| Migrations            | Flyway                         |
| API Docs              | Springdoc OpenAPI (Swagger UI) |
| Boilerplate reduction | Lombok                         |
| Build tool            | Maven                          |

---

## Project Structure

```
src/
└── main/
    ├── java/com/example/api/
    │   ├── ApiApplication.java          ← Entry point (@SpringBootApplication)
    │   │
    │   ├── controller/
    │   │   ├── AuthController.java      ← POST /api/auth/register, /api/auth/login
    │   │   ├── PostController.java      ← CRUD for /api/posts
    │   │   ├── CommentController.java   ← CRUD for /api/posts/{id}/comments
    │   │   └── HealthController.java    ← GET /health
    │   │
    │   ├── service/
    │   │   ├── AuthService.java         ← Register, login, token generation
    │   │   ├── PostService.java         ← Post business logic
    │   │   ├── CommentService.java      ← Comment business logic
    │   │   └── JwtService.java          ← Token creation and validation
    │   │
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── PostRepository.java
    │   │   └── CommentRepository.java
    │   │
    │   ├── model/
    │   │   ├── User.java                ← users table
    │   │   ├── Post.java                ← posts table
    │   │   └── Comment.java             ← comments table (FK → posts)
    │   │
    │   ├── dto/
    │   │   ├── RegisterRequest.java
    │   │   ├── LoginRequest.java
    │   │   ├── AuthResponse.java
    │   │   ├── PostRequest.java
    │   │   ├── PostResponse.java
    │   │   ├── CommentRequest.java
    │   │   └── CommentResponse.java
    │   │
    │   ├── config/
    │   │   ├── SecurityConfig.java      ← Auth rules, filter chain
    │   │   ├── JwtAuthFilter.java       ← Validates Bearer token on each request
    │   │   └── OpenApiConfig.java       ← Swagger UI metadata
    │   │
    │   └── exception/
    │       ├── ResourceNotFoundException.java
    │       └── GlobalExceptionHandler.java  ← Returns clean JSON errors
    │
    └── resources/
        ├── application.properties           ← Local config (H2)
        ├── application-prod.properties      ← Production config (PostgreSQL)
        └── db/migration/
            ├── V1__create_posts_table.sql
            ├── V2__create_users_table.sql
            ├── V3__create_comments_table.sql
            ├── V4__add_indexes.sql
            └── V5__seed_sample_data.sql
```

---

## Analogy Map (Django / FastAPI → Spring Boot)

| Concept             | Django                       | FastAPI                     | Spring Boot              |
|---------------------|------------------------------|-----------------------------|--------------------------|
| Entry point         | `manage.py`                  | `main.py`                   | `ApiApplication.java`    |
| Routing + HTTP      | `views.py` + `urls.py`       | `@router`                   | `@RestController`        |
| Business logic      | fat views / `services.py`    | service functions           | `@Service` class         |
| DB queries          | `Model.objects`              | SQLAlchemy session          | `JpaRepository`          |
| DB table definition | `models.py`                  | SQLAlchemy model            | `@Entity` class          |
| Input validation    | Serializer / Form            | Pydantic model              | DTO + `@Valid`           |
| Output shape        | Serializer (read)            | `response_model=`           | Response DTO             |
| Error handling      | `exception_handler`          | `@app.exception_handler`    | `@RestControllerAdvice`  |
| Config              | `settings.py`                | `.env` / config             | `application.properties` |
| Migrations          | `makemigrations` + `migrate` | Alembic                     | Flyway `.sql` files      |
| Auth middleware     | `@login_required`            | `Depends(get_current_user)` | `JwtAuthFilter`          |

---

## Prerequisites

- Java 21+ — [adoptium.net](https://adoptium.net) (get Temurin 21 LTS)
- Maven 3.8+ — [maven.apache.org](https://maven.apache.org)

```bash
java -version   # should print openjdk 21+
mvn -version    # should print Apache Maven 3.x
```

**macOS:**
```bash
brew install openjdk@21 maven
```

**Ubuntu / Debian:**
```bash
sudo apt install openjdk-21-jdk maven
```

---

## Running Locally

```bash
mvn spring-boot:run
```

First run downloads dependencies (~1 min). Subsequent runs are fast. The app starts on port `8080` using an in-memory H2 database — no setup required.

```
Started ApiApplication in 2.3 seconds
```

**Useful local URLs:**

| URL                                     | Description          |
|-----------------------------------------|----------------------|
| `http://localhost:8080/`                | Welcome message      |
| `http://localhost:8080/health`          | Health check         |
| `http://localhost:8080/swagger-ui.html` | Interactive API docs |
| `http://localhost:8080/h2-console`      | Database browser     |

**H2 Console connection settings:**
```
JDBC URL:  jdbc:h2:mem:devdb
Username:  sa
Password:  (leave empty)
```

---

## API Reference

### Auth

| Method | Endpoint             | Auth | Description          |
|--------|----------------------|------|----------------------|
| `POST` | `/api/auth/register` | None | Create a new account |
| `POST` | `/api/auth/login`    | None | Get a JWT token      |

### Posts

| Method   | Endpoint                    | Auth             | Description               |
|----------|-----------------------------|------------------|---------------------------|
| `GET`    | `/api/posts`                | None             | List all posts            |
| `GET`    | `/api/posts?search=keyword` | None             | Search posts by title     |
| `GET`    | `/api/posts/published`      | None             | List published posts only |
| `GET`    | `/api/posts/{id}`           | None             | Get one post              |
| `POST`   | `/api/posts`                | Required         | Create a post             |
| `PUT`    | `/api/posts/{id}`           | Required         | Update a post             |
| `DELETE` | `/api/posts/{id}`           | Required (ADMIN) | Delete a post             |

### Comments

| Method   | Endpoint                            | Auth             | Description             |
|----------|-------------------------------------|------------------|-------------------------|
| `GET`    | `/api/posts/{postId}/comments`      | None             | List comments on a post |
| `POST`   | `/api/posts/{postId}/comments`      | Required         | Add a comment           |
| `DELETE` | `/api/posts/{postId}/comments/{id}` | Required (ADMIN) | Delete a comment        |

---

## Example Requests

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "password": "secret123"}'
```

**Login and capture token:**
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "password": "secret123"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
```

**Create a post:**
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title": "Hello World", "content": "My first post.", "author": "Alice", "published": true}'
```

**Add a comment:**
```bash
curl -X POST http://localhost:8080/api/posts/1/comments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"content": "Great post!", "author": "Bob"}'
```

---

## Authentication

The API uses JWT Bearer tokens. Include the token in the `Authorization` header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Tokens expire after 24 hours. Re-authenticate via `/api/auth/login` to get a new one.

**Roles:**

| Role    | Can do                              |
|---------|-------------------------------------|
| `USER`  | Read all, create posts and comments |
| `ADMIN` | Everything USER can do, plus delete |

---

## Database Migrations

Migrations live in `src/main/resources/db/migration/` and follow Flyway's naming convention:

```
V{version}__{description}.sql
```

On startup, Flyway runs any migration it hasn't seen before, in version order. Migrations that have already run are never modified — add a new file to make schema changes.

**To add a new migration:**
```bash
# Create the file
touch src/main/resources/db/migration/V6__add_email_to_users.sql

# Write the SQL
echo "ALTER TABLE users ADD COLUMN email VARCHAR(255);" > \
  src/main/resources/db/migration/V6__add_email_to_users.sql

# Then update the User entity to match, and restart
```

---

## Running Tests

```bash
mvn test
```

Tests use H2 in-memory database — no external dependencies needed. Coverage includes: list endpoints, create, 404 handling, and validation errors.

---

## Deploying to Railway

### 1. Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/yourname/your-repo.git
git push -u origin main
```

### 2. Create Railway project

- Go to [railway.app](https://railway.app) → New Project → Deploy from GitHub repo
- Select your repository

### 3. Add PostgreSQL

- Inside your project → New Service → Database → PostgreSQL
- Railway sets `DATABASE_URL` automatically

### 4. Set environment variables

In your app service → Variables:

```
SPRING_PROFILES_ACTIVE = prod
JWT_SECRET             = your-long-random-secret-here
```

### 5. Deploy

Railway detects the Maven project, runs `mvn package`, and starts the app using the command in `railway.json`. The `/health` endpoint is used as the health check.

---

## Configuration

| Property    | Local         | Production                         |
|-------------|---------------|------------------------------------|
| Database    | H2 in-memory  | PostgreSQL (via `DATABASE_URL`)    |
| `ddl-auto`  | `create-drop` | `validate` (Flyway manages schema) |
| H2 console  | Enabled       | Disabled                           |
| SQL logging | Enabled       | Disabled                           |
| Port        | `8080`        | `$PORT` (set by Railway)           |

**Never commit secrets.** In production, `jwt.secret` is read from the `JWT_SECRET` environment variable.

---

## What to Build Next

- **Email verification** — send a confirmation email on register using Spring Mail
- **Pagination** — replace `findAll()` with `findAll(Pageable)` for large datasets
- **Refresh tokens** — issue short-lived access tokens + long-lived refresh tokens
- **Rate limiting** — add `bucket4j` to throttle requests per user
- **Caching** — annotate service methods with `@Cacheable` using Redis or Caffeine