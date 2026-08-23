# Unsent API — Release Notes

**Version:** 1.0.0-SNAPSHOT
**Component:** unsent-api

## Summary

This release establishes the core backend platform for Unsent, an online
diary and reaction platform. It introduces user authentication, diary
journaling, collaborative storytelling, AI-assisted writing, a social
graph, real-time messaging, and push notifications, backed by a new
application-level caching layer for read-heavy workloads.

## Features Delivered

### Identity & Access Management
Email/password and OAuth2-based authentication and registration, with
JWT-based session issuance and user profile management.

### Diary Journaling
Full lifecycle management of personal diary entries — creation, editing,
deletion, retrieval, and keyword search — forming the core journaling
experience of the product.

### Collaborative Storytelling
A contribution workflow that lets users submit, accept, or reject
additions to another user's diary entry, enabling collaborative
narratives to form around a single story thread.

### AI-Assisted Story Continuation
Integration with a large-language-model provider to generate AI-suggested
continuations of a user's story, giving users a creative starting point
when they're stuck.

### Social Graph
A friend request and relationship system — sending, accepting, rejecting,
and querying friendship status — that underpins visibility and sharing
across the platform.

### Real-Time Messaging
WebSocket-based chat infrastructure allowing users to exchange messages
and retrieve conversation history in real time.

### Push Notifications
A notification subscription service to register and manage user devices
for push delivery, laying the groundwork for engagement notifications.

### Performance & Reliability
Introduced an application-level caching layer across the most-read
services (diary, chat, friends, contributions) to reduce database load,
plus a corrected build configuration ensuring cache keys resolve
reliably at runtime.

### Delivery Pipeline
Containerized build (Docker) and automated CI pipeline for consistent,
repeatable deployments.

## Tools & Technology Stack

| Category | Tool / Library | Version |
|---|---|---|
| Language | Java | 21 |
| Application Framework | Spring Boot (BOM) | 4.0.2 |
| Web Layer | Spring Web MVC | via Spring Boot 4.0.2 |
| Real-Time Messaging | Spring WebSocket | via Spring Boot 4.0.2 |
| Security | Spring Security + OAuth2 Client / Resource Server | via Spring Boot 4.0.2 |
| Auth Tokens | JJWT (jjwt-api / impl / jackson) | 0.11.5 |
| Persistence | Spring Data JPA | via Spring Boot 4.0.2 |
| Database | PostgreSQL (JDBC driver) | 42.7.9 |
| Schema Migrations | Flyway (+ flyway-database-postgresql) | 11.14.1 |
| Caching | Spring Cache abstraction + Caffeine | via Spring Boot 4.0.2 |
| AI Integration | OpenAI Java SDK | 2.8.1 |
| Boilerplate Reduction | Lombok | 1.18.32 |
| Build Tool | Apache Maven (maven-compiler-plugin) | 3.11.0 |
| Test Database | H2 (in-memory) | via Spring Boot 4.0.2 |
| Observability | Spring Boot Actuator | via Spring Boot 4.0.2 |
| Containerization | Docker | — |
| CI/CD | GitHub Actions / Jenkins | — |

## Fixes & Hardening

- Resolved a set of caching defects where cache keys either referenced
  non-existent method parameters or were hardcoded to a constant value,
  causing unrelated users' data to collide in the cache or the cache
  layer to fail at runtime.
- Corrected a cache-eviction mismatch that left newly created diary
  entries missing from cached list results.
- Fixed a build configuration gap (missing `-parameters` compiler flag)
  that prevented the caching layer from resolving named parameters
  reliably.

## Known Follow-ups

- A friend-request validation routine currently validates the sender
  twice instead of validating both sender and receiver — scheduled for
  a follow-up fix.
