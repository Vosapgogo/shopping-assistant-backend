# AI Shopping Assistant — Backend

Java + Spring Boot backend для [AI Shopping Assistant](../PROJECT_DOCUMENTATION.md). Повна архітектура й теорія — у `PROJECT_DOCUMENTATION.md` у корені проєкту, покроковий план — у `ROADMAP.md`.

## Стек
Java 17, Spring Boot, Spring Web, Spring Data JPA, PostgreSQL, Flyway, Spring Validation, Spring Boot Actuator, Lombok.

## Структура пакетів
Модульний моноліт за бізнес-доменами (не за технічними шарами):
```
com.shoppingassistant
├── health          (GET /api/v1/health)
├── auth            (api / application / domain / infrastructure)
├── shopping
├── ai
├── pricing
├── catalog
├── routing         (Plus)
├── fridge          (Experimental)
└── notification    (Experimental)
```
Модулі, поки не мають логіки (наповнюються поступово, по спринтах з ROADMAP.md), містять лише порожні пакети-заглушки (`.gitkeep`).

Правила залежностей між модулями — PROJECT_DOCUMENTATION.md, розділ 3.3.

## Як запустити локально

1. Скопіювати `.env.example` → `.env` і заповнити за потреби (для Sprint 0 дефолтні значення підходять).
2. Підняти PostgreSQL:
   ```bash
   docker compose up -d
   ```
3. Запустити застосунок:
   ```bash
   ./gradlew bootRun
   ```
4. Перевірити:
   ```bash
   curl localhost:8080/api/v1/health
   curl localhost:8080/actuator/health
   ```

## Тести
```bash
./gradlew test
```

## Профілі
`application.yml` (спільне) + `application-local.yml` / `application-test.yml` / `application-prod.yml`. Активний профіль — через `SPRING_PROFILES_ACTIVE` (за замовчуванням `local`).

## CI
GitHub Actions (`.github/workflows/backend-ci.yml`): compile → unit/integration tests (проти PostgreSQL-сервісу) → build jar.
