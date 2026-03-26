# dispatch-accounts

A small, focused customer account service built as part of the broader `dispatch` ecosystem.

This project exists as a hands-on exploration of core backend concepts:

- RESTful service design
- data modeling with Postgres
- schema management with Flyway
- password hashing vs encryption
- clean separation between API, service, and persistence layers

The goal is not completeness, but clarity — building a system piece by piece, with each concept grounded in real
implementation.

---

## Current Features

- Create account (`POST /v1/accounts`)
- Password hashing using BCrypt
- Flyway-managed schema
- Postgres-backed persistence
- Clean DTO separation (API ↔ service ↔ entity)

---

## Project Structure

```txt
dispatch-accounts-deploy/
    docker-compose.yml # Postgres + Redis
    scripts/ # Manual test scripts (curl)

dispatch-accounts-kt/
    src/main/kotlin/... # Spring Boot application
    src/main/resources/
    db/migration/ # Flyway migrations
````

---

## Running Locally

Start infrastructure:

`docker compose up -d`

Start the application:

`./gradlew bootRun`

### Database

Postgres runs on localhost:5433
Database: dispatch_accounts
Schema managed via Flyway

To reset:

`docker compose down -v`

`docker compose up -d`

### Manual Testing

#### Create an account:

`./scripts/create-account.sh`

#### Inspect stored data:

```bash
PGPASSWORD=dispatch psql -h localhost -p 5433 -U dispatch -d dispatch_accounts \
  -c "SELECT id, email, password_hash FROM accounts;"
```

## Notes

This service intentionally separates concerns:

- API layer handles HTTP + validation
- Service layer handles business logic (hashing, encryption)
- Persistence layer handles storage

Passwords are hashed (one-way), while other sensitive fields may be encrypted (two-way), depending on use case.

### Roadmap

- Add encryption for sensitive fields
- Implement account lookup endpoints
- Introduce caching with Redis
- Add authentication flow (login)
- Explore gateway/service boundary

### Philosophy

#### This project favors:

- small, understandable steps
- explicit over implicit behavior
- learning through implementation, not abstraction

__It is meant to feel like building something real — just on a smaller scale.__