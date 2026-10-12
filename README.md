# Show Booking System

A Java 17 and Spring Boot API for practicing show discovery and ticket-booking workflows. The code includes user registration/login, show browsing, booking actions, and domain models for tickets, slots, and waitlists.

## Stack

- Java 17
- Spring Boot
- Spring Data JPA and Hibernate
- H2 in-memory database
- Spring Security and JWT

## Run locally

Requirements: Java 17 and Maven.

```bash
mvn spring-boot:run
```

The app uses an in-memory H2 database, so data is reset when the process restarts. The H2 console is enabled for local development.

## Security note

This is an interview/learning project, not a production-ready booking service. A JWT signing key is committed in application configuration; treat it as exposed, rotate it before any deployment, and move secrets to environment-backed configuration. Review endpoint authorization and booking concurrency before exposing the service to users.
