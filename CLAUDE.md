# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

Peecko Admin (`peecko-one`) is a B2B back-office application for managing wellness/fitness app subscriptions. An **Agency** (Peecko itself) manages **Customers** (companies), who have **ApsPlans** (subscription contracts), which generate monthly **ApsOrders**, which produce **Invoices** sent by email as PDFs. Mobile app users (**ApsUsers**) and their devices (**ApsDevices**) are tracked per Customer.

Generated with JHipster 8.1.0. Stack: Spring Boot 3.2.0 (Java 17), Angular 17, PostgreSQL, Liquibase, JWT auth.

## Commands

### Running locally (two terminals)

```bash
# Terminal 1 — backend (dev profile, port 8080)
./mvnw

# Terminal 2 — frontend dev server (port 4200, proxies API to 8080)
npm start
```

Requires PostgreSQL at `localhost:5432/admin` (username: `admin`, no password). Start with Docker:
```bash
npm run docker:db:up
```

### Building

```bash
# Dev build (with tests, requires Docker for Testcontainers)
./mvnw -Pdev clean verify

# Production jar
./mvnw -Pprod clean verify

# Production frontend only
npm run webapp:prod
```

### Testing

```bash
# All backend tests (uses Testcontainers — requires Docker)
./mvnw verify

# Single backend test class
./mvnw -Dtest=AgencyResourceIT test

# All frontend tests (Jest)
npm test

# Single frontend test file
npx jest src/main/webapp/app/entities/aps-order/list/aps-order.component.spec.ts

# Frontend tests in watch mode
npm run test:watch
```

### Linting and formatting

```bash
npm run lint          # ESLint
npm run lint:fix      # ESLint with auto-fix
npm run prettier:check
npm run prettier:format
```

## Architecture

### Multi-tenancy by Agency

Every backend service call is scoped to the logged-in user's agency. `UserService.getCurrentAgencyId()` reads the `agency_id` column on `jhi_user` and all service methods apply it as a filter. REST controllers never accept an `agencyId` parameter from the client — it is always derived from the JWT session.

### Backend layers

```
web/rest/        — REST controllers (@RestController), one per entity
service/         — Business logic; references repositories and other services
service/specs/   — JPA Specification builders for dynamic queries (e.g., ApsOrderSpecs)
service/request/ — Plain DTOs used as filter parameters passed from controllers to services
domain/          — JPA entities; fluent builder-style setters (entity.field(value) returns entity)
domain/dto/      — Transfer objects not mapped to DB tables
domain/enumeration/ — Enums: PlanState, PricingType, CustomerState, CountryName, etc.
repository/      — Spring Data JPA repositories
```

### Business logic flow: monthly billing

1. **`ApsOrderService.batchOrders(period, contract)`** — finds all active `ApsPlan`s for a period and creates an `ApsOrder` record for each (if not already present). Orders start with `numberOfUsers = 0`.
2. **Members upload** — CSV upload via `ApsOrderResource` sets `numberOfUsers` on the order. Each row creates an `ApsMembership`.
3. **`InvoiceService.batchInvoiceForAgency/Contract`** — for orders with subscribers, creates an `Invoice` + `InvoiceItem` using pricing from `ApsPricing` (lookup table by country/customer/quantity and `PricingType`: FIXED, FITNESS, WELLNESS).
4. **Invoice PDF** — `InvoicePdfService` renders a Thymeleaf HTML template with country-aware locale/currency (from `CountryService`) then converts to PDF via `InvoicePdfGeneratorService`.
5. **Invoice email** — `InvoiceEmailService` sends the PDF as an attachment to the customer's `billingEmail`.

Invoice number format: `PCK{period}{count:03d}` (e.g., `PCK202401001`). Period is an integer `YYYYMM`.

### Plan lifecycle

`ApsPlan.state` transitions: `TRIAL → ACTIVE → CLOSED`. `ApsPlanTaskService.closeExpiredPlans()` handles auto-close of expired plans (scheduled task currently disabled — see `ScheduledTasks.java`).

### Frontend structure

Angular 17 with **standalone components** (no NgModules). Each entity under `src/main/webapp/app/entities/{entity-name}/` has:

- `{entity}.model.ts` — TypeScript interfaces, plus `{Entity}Access` and `{ENTITY}_USER_ACCESS` for field-level control
- `service/` — HTTP service wrapping the REST API
- `list/`, `detail/`, `update/`, `delete/` — standard CRUD components
- `route/` — route resolver for loading entity by ID

**`FieldAccess`** (`shared/profile/view.models.ts`) controls per-field rendering: `{ listable, visible, disabled }`. Each entity model defines a `XXXAccess` interface and a `XXX_USER_ACCESS` constant used in update forms and list views to enable/disable and show/hide fields.

Some entities have additional sub-components (e.g., `aps-order/members/` for CSV upload dialog, `agency/agency-select/` for select dialogs used in other forms).

### Database migrations

Liquibase changelogs live in `src/main/resources/config/liquibase/changelog/` named `2024.NNN.entity_Name.xml`. New schema changes go here in sequence. Initial/seed data is in `init_data/` and loaded under the `init_data` Liquibase context (active in dev profile).

### Country / locale / currency

`Country` entity (PK: `code` ISO 3166-1 alpha-2) stores `locale`, `currency` (ISO 4217), and `language`. `CountryService` looks up a country for invoice PDF rendering. Default fallback is Luxembourg (EUR). The `CountryName` enum lists all 27 EU member states.

### Configuration profiles

- **dev** — `application-dev.yml`: connects to `localhost:5432/admin`, DEBUG logging, Spring DevTools
- **prod** — `application-prod.yml`: production DB, INFO logging
- Mail configured in `application.yml` (SMTP); overridden per profile

### AWS deployment

Production infrastructure docs and scripts are in `aws/vpc_prod/`. The app is packaged as a JAR and deployed to EC2 in a VPC, with RDS PostgreSQL and Secrets Manager for credentials.