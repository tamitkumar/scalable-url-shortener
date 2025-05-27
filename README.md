# ✅ scalable-url-shortener
Design a scalable URL shortener like bit.ly

# ✅ URL Shortener Service

## 🔧 Use Case
### You have a long URL like:
#### - https://www.youtube.com/watch?v=dQw4w9WgXcQ
### And you want to shorten it to something like:
#### - http://localhost:8080/api/url/abc123
#### When someone visits that short URL (abc123), they are redirected to the original long URL.

## 🚀 Features
| Feature              | Description                                               |
|----------------------|-----------------------------------------------------------|
| Shorten URL          | Converts long URL to short one                            |
| Redirect             | Short URL automatically redirects to long URL             |
| Cache with Redis     | Speeds up redirection by avoiding DB every time           |
| Persistent with DB   | Stores mapping in PostgreSQL                              |
| Scalable Architecture| Designed to support 100M+ URLs (horizontal scale)         |

## ✅ Overview
A scalable URL shortener implemented in Spring Boot with PostgreSQL and Redis.

Features:

- Shorten URLs (Base62 encoding)
- Redirect short URLs to original URLs
- Click analytics tracking
- Swagger UI for API testing
- Docker Compose for Postgres & Redis
- Java-based Spring configuration (no application.yml/properties)

---

## ✅ Prerequisites

- Docker & Docker Compose installed
- Java 17+ & Gradle installed (for local runs)

---
# 🧩 Step-by-Step Plan
## ✅ Step 1: Project Setup
- Spring Boot (Gradle)
- Dependencies: Web, Data JPA, Redis, PostgreSQL, Swagger

## ✅ Step 2: Entities & Repositories
- ShortUrlEntity: Maps short URL to original URL, created timestamp, click count, etc.

## ✅ Step 3: Base62 Encoder
- Encode DB ID to short URL string (like abc123)

## ✅ Step 4: Controller APIs
- POST /shorten: Accepts original URL, returns short URL
- GET /{shortCode}: Redirects to original URL and counts visit

## ✅ Step 5: Caching with Redis
- Shortcode ↔ original URL mapping in Redis for fast redirects

## ✅ Step 6: Swagger UI Integration
- Use Springfox or springdoc-openapi for API testing

## ✅ Step 7: Docker Compose Setup
- PostgreSQL + Redis setup with volumes and environment config

## ✅ Step 8: Visit Analytics
- Count clicks per short URL, store in DB, show via API

## ✅ Tech Breakdown

| Layer       | Technology     | Role                             |
|-------------|----------------|----------------------------------|
| Backend     | Spring Boot     | REST API server                  |
| DB          | PostgreSQL      | Stores `id` ↔ long URL           |
| Cache       | Redis           | Stores short code ↔ long URL     |
| Encoding    | Base62          | Converts DB ID to short code     |
| API Testing | Postman / cURL  | Testing API endpoints            |	       Testing API endpoints

## ✅ Running with Docker Compose

1. Start Postgres & Redis:

```bash
docker-compose up -d
```

## ✅ Build and run the Spring Boot app locally:
```bash
./mvnw spring-boot:run
# or
./gradlew bootRun
```

## ✅ Using Docker to build and run app container
- Build app Docker image:
```bash
docker build -t urlshortener-app .
```
- Run app container (linking to docker-compose network):
```bash
docker network create urlshortener-network
docker run -d --network urlshortener-network -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/urlshortener \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  urlshortener-app
```

## ✅ API Endpoints
- POST /shorten
  Request: { "originalUrl": "https://example.com" }
  Response: { "shortCode": "abc123" }
- GET /{shortCode}
  Redirects to original URL and increments click count
- GET /analytics/{shortCode}
  Returns click count and details for shortCode

## ✅ Swagger UI
- Open in browser: http://localhost:8080/swagger-ui.html

## ✅ Key files explained:
- UrlShortenerApplication.java — Spring Boot main class
- config/DataSourceConfig.java — Java config for Postgres datasource
- config/RedisConfig.java — Java config for RedisTemplate
- config/SwaggerConfig.java — Swagger UI config
- controller/UrlShortenerController.java — REST APIs (/shorten, /analytics/{code}, /{code})
- entity/ShortUrlEntity.java — JPA entity for URL mapping
- repository/ShortUrlRepository.java — JPA repository
- service/UrlShortenerService.java — Business logic including click counting
- util/Base62Encoder.java — Encode/decode ID to base62 short code
- docker-compose.yml — Postgres + Redis setup
- Dockerfile — Build Spring Boot app container


## 📥 What is the Input?

You send an HTTP POST request:

POST http://localhost:8080/api/url/shorten?originalUrl=https://www.youtube.com/watch?v=dQw4w9WgXcQ

✅ You receive this output:

"http://localhost:8080/api/url/abc123"

---

## 📤 What is the Output?

You now use the short URL:

GET http://localhost:8080/api/url/abc123

✅ It will redirect (302 Found) you to:

https://www.youtube.com/watch?v=dQw4w9WgXcQ

---

## 🔁 Real Example Flow (Frontend or Postman)

Step 1: POST /shorten  
Request:

POST /api/url/shorten?originalUrl=https://openai.com

Response:

"http://localhost:8080/api/url/abc001"

---

Step 2: GET /abc001  
Request:

GET /api/url/abc001

Browser Output:  
✅ Automatically redirects to:

https://openai.com


###  Inter inside docker
```bash
docker exec -it url_shortener_postgres psql -U tinyuser -d tinyurl_db
```
### Inter inside docker cmd to access database
```bash
psql -h localhost -p 5433 -U tinyuser -d tinyurl_db
```
### shutdown docker with compose file
```bash
docker-compose down -v 
```
### Start docker with compose file
```bash
docker-compose up -d 
```
### get available container details
```bash
docker ps

CONTAINER ID   IMAGE         COMMAND                  CREATED       STATUS          PORTS                    NAMES
3d51c4bf5f22   postgres:15   "docker-entrypoint.s…"   2 hours ago   Up 48 minutes   0.0.0.0:5433->5432/tcp   url_shortener_postgres
ac097c65ae40   redis:7       "docker-entrypoint.s…"   2 hours ago   Up 48 minutes   0.0.0.0:6379->6379/tcp   url_shortener_redis
```
### Inter inside redis cli
```bash
docker exec -it 3d51c4bf5f22 redis-cli
```
```bash
127.0.0.1:6379> KEYS *                                          <---------get all key
1) "url:4"
2) "url:1"
3) "url:3"
127.0.0.1:6379> GET "url:4"                                     <---------To see the value for a specific key:
"https://www.linkedin.com/in/amit-kumar-tiwari-02b88061/"
```
