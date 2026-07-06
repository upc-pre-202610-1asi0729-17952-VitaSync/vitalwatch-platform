# VitalWatch Platform API

VitalWatch Platform API is a Spring Boot backend for a healthcare monitoring platform focused on medical staff fatigue prevention, shift coordination, clinical risk assessment, staff recovery, subscriptions, auditability, and invitation-based access.

The project follows a modular structure inspired by Domain-Driven Design (DDD), organizing the source code by bounded contexts and separating REST interfaces, persistence, domain models, application contracts, and shared infrastructure.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Implemented Bounded Contexts](#implemented-bounded-contexts)
- [Main Features](#main-features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Environment Variables](#environment-variables)
- [Run Locally](#run-locally)
- [Run with Docker](#run-with-docker)
- [Swagger Documentation](#swagger-documentation)
- [Authentication Flow](#authentication-flow)
- [API Endpoint Summary](#api-endpoint-summary)
- [Email Invitations with Resend](#email-invitations-with-resend)
- [Frontend Integration Notes](#frontend-integration-notes)
- [Recommended Validation Checklist](#recommended-validation-checklist)
- [Git Workflow](#git-workflow)
- [Author](#author)
- [License](#license)

---

## Project Overview

VitalWatch is a healthcare platform designed to help medical centers monitor fatigue and reduce burnout risks among healthcare staff.

This backend provides the API layer for:

- Authentication and JWT-based authorization.
- Organization and user management.
- Subscription and billing simulation.
- Invitation-based staff registration.
- Medical specialty and work area catalogs.
- Care team and shift coordination.
- Fatigue and clinical risk assessment.
- Vital sign readings and anomalies.
- Staff recovery and preventive actions.
- Audit logs and operational traceability.
- Email invitations through Resend.

Base API path:

```txt
/api/v1
```

Local Swagger URL:

```txt
http://localhost:8080/api/v1/swagger-ui.html
```

Alternative Swagger URL:

```txt
http://localhost:8080/api/v1/swagger-ui/index.html
```

OpenAPI JSON:

```txt
http://localhost:8080/api/v1/api-docs
```

---

## Tech Stack

- Java 26
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT authentication
- MySQL
- Maven Wrapper
- Docker
- Docker Compose
- Swagger / OpenAPI
- Resend Email API
- Lombok
- i18n resource bundles

---

## Implemented Bounded Contexts

```txt
src/main/java/com/vitalwatch/center/platform/
├── iam
├── subscriptions
├── billing
├── shiftcoordination
├── clinicalriskassessment
├── staffrecovery
├── auditcompliance
└── shared
```

### IAM

Handles authentication, organizations, users, roles, invitations, work areas, specialties, and registration through invitation links.

### Subscriptions and Billing

Handles available plans, subscriptions, checkout sessions, checkout status, and subscription activation.

### Shift Coordination

Handles clinical care teams, team members, shift records, check-in, check-out, and shift status updates.

### Clinical Risk Assessment

Handles risk assessments, clinical alerts, vital sign readings, and vital sign anomalies.

### Staff Recovery

Handles preventive recovery actions assigned to medical staff.

### Audit Compliance

Handles audit logs and traceability of critical platform events.

### Shared

Contains cross-cutting infrastructure such as security, error handling, i18n, OpenAPI configuration, and common response helpers.

---

## Main Features

- JWT sign-in and protected endpoints.
- Role-based access control for:

```txt
HOSPITAL_ADMIN
SUPERVISOR
DOCTOR
```

- Public endpoints for selected flows such as plan listing, checkout simulation, invitation validation, invitation acceptance, specialties, and work areas.
- Invitation email delivery using Resend.
- Configurable invitation links for local and production frontend environments.
- Dockerized backend and MySQL database.
- Swagger documentation with Bearer authentication support.
- Seeded sample data for testing dashboards and API flows.
- i18n messages in English and Spanish.

---

## Project Structure

Each bounded context follows a similar structure:

```txt
bounded-context/
├── domain/
│   └── model/
│       └── enums/
├── infrastructure/
│   └── persistence/
│       └── jpa/
│           ├── entities/
│           ├── repositories/
│           └── seed/
└── interfaces/
    └── rest/
        ├── controllers/
        ├── resources/
        └── transform/
```

Shared infrastructure:

```txt
shared/
├── application/
│   ├── i18n/
│   └── result/
├── infrastructure/
│   ├── documentation/
│   ├── security/
│   └── i18n/
└── interfaces/
    └── rest/
```

---

## Prerequisites

Install the following tools before running the project:

- Java 26
- Maven, or use the included Maven Wrapper
- MySQL 8.x
- Docker Desktop
- Git
- IntelliJ IDEA or another Java IDE

Check Java:

```bash
java -version
```

Check Docker:

```bash
docker --version
docker compose version
```

---

## Environment Variables

The project is designed to read sensitive or environment-specific values from environment variables.

Common local variables:

```txt
DATABASE_URL=localhost
DATABASE_PORT=3306
DATABASE_NAME=vitalwatch_platform
DATABASE_USER=root
DATABASE_PASSWORD=your_mysql_password
```

Email invitation variables:

```txt
RESEND_ENABLED=false
RESEND_API_KEY=
RESEND_FROM_EMAIL=VitalWatch <onboarding@resend.dev>
APP_PUBLIC_URL=http://localhost:4200
EMAIL_LOGO_URL=https://vitalwatch.space/assets/images/logo.png
```

For local email testing with Resend:

```txt
RESEND_ENABLED=true
RESEND_API_KEY=your_resend_api_key
RESEND_FROM_EMAIL=VitalWatch <invitations@notifications.vitalwatch.space>
APP_PUBLIC_URL=http://localhost:4200
EMAIL_LOGO_URL=https://vitalwatch.space/assets/images/logo.png
```

For production frontend invitation links:

```txt
APP_PUBLIC_URL=https://app.vitalwatch.space
```

For production email sender:

```txt
RESEND_FROM_EMAIL=VitalWatch <invitations@notifications.vitalwatch.space>
```

Do not commit real API keys, database passwords, or production secrets.

---

## Run Locally

### 1. Clone the repository

```bash
git clone <repository-url>
cd vitalwatch-platform
```

### 2. Create the local MySQL database

```sql
CREATE DATABASE IF NOT EXISTS vitalwatch_platform;
```

### 3. Configure environment variables

Set the database variables in your IDE Run Configuration or operating system.

Example for IntelliJ Run Configuration:

```txt
DATABASE_URL=localhost
DATABASE_PORT=3306
DATABASE_NAME=vitalwatch_platform
DATABASE_USER=root
DATABASE_PASSWORD=your_mysql_password
RESEND_ENABLED=false
APP_PUBLIC_URL=http://localhost:4200
EMAIL_LOGO_URL=https://vitalwatch.space/assets/images/logo.png
```

### 4. Build the project

```bash
./mvnw -DskipTests clean package
```

On Windows PowerShell:

```powershell
.\mvnw.cmd -DskipTests clean package
```

### 5. Run the project

Run the main Spring Boot application from your IDE.

The application starts at:

```txt
http://localhost:8080
```

Swagger:

```txt
http://localhost:8080/api/v1/swagger-ui.html
```

---

## Run with Docker

Before using Docker, stop the backend if it is running in IntelliJ because both use port `8080`.

### 1. Stop previous containers

```powershell
docker compose down
```

### 2. Build the project

```powershell
.\mvnw.cmd -DskipTests clean package
```

### 3. Start Docker services

```powershell
docker compose up --build
```

Docker should start:

```txt
vitalwatch-mysql
vitalwatch-platform
```

Check status:

```powershell
docker compose ps
```

Expected state:

```txt
vitalwatch-mysql       Up healthy
vitalwatch-platform    Up
```

Swagger in Docker:

```txt
http://localhost:8080/api/v1/swagger-ui.html
```

### 4. Stop Docker services

```powershell
docker compose down
```

---

## Swagger Documentation

Swagger UI is available at:

```txt
http://localhost:8080/api/v1/swagger-ui.html
```

OpenAPI JSON is available at:

```txt
http://localhost:8080/api/v1/api-docs
```

Most endpoints require authentication.

Use:

```txt
POST /authentication/sign-in
```

Then copy the returned JWT token and click the Swagger **Authorize** button.

Use this format:

```txt
Bearer <your-token>
```

---

## Authentication Flow

### Sign In

```txt
POST /authentication/sign-in
```

Example body:

```json
{
  "email": "admin@vitalwatch.com",
  "password": "admin123"
}
```

The response includes an authentication token.

### Current User

```txt
GET /authentication/me
```

Requires Bearer token.

### Sign Up

```txt
POST /authentication/sign-up
```

Used for organization administrator registration flows.

### Invitation Acceptance

```txt
POST /invitations/accept
```

Used when a user receives an invitation link and creates an account.

---

## API Endpoint Summary

### Authentication

```txt
POST /authentication/sign-in
POST /authentication/sign-up
GET  /authentication/me
```

### Organizations

```txt
GET  /organizations
GET  /organizations/{organizationId}
POST /organizations
```

### Users

```txt
GET   /users
GET   /users/{userId}
POST  /users
PATCH /users/{userId}
```

### Plans

```txt
GET /plans
GET /plans/{planId}
GET /plans/by-code/{code}
```

### Subscriptions

```txt
GET  /subscriptions
GET  /subscriptions/{subscriptionId}
GET  /subscriptions/organization/{organizationId}
POST /subscriptions
```

### Billing

```txt
POST /billing/create-checkout-session
GET  /billing/checkout-session-status
POST /billing/activate-checkout-session
```

### Invitations

```txt
GET  /invitations
GET  /invitations/{invitationId}
GET  /invitations/by-token/{token}
POST /invitations/send
POST /invitations/accept
```

### Catalogs

```txt
GET /specialties
GET /specialties/{specialtyId}

GET /workAreas
GET /workAreas?organizationId={organizationId}
GET /workAreas/{workAreaId}
```

### Shift Coordination

```txt
GET    /careTeams
GET    /careTeams/{careTeamId}
POST   /careTeams
PATCH  /careTeams/{careTeamId}
DELETE /careTeams/{careTeamId}

GET    /teamMembers
GET    /teamMembers/{teamMemberId}
POST   /teamMembers
DELETE /teamMembers/{teamMemberId}

GET   /shiftRecords
GET   /shiftRecords/{shiftRecordId}
POST  /shiftRecords
PATCH /shiftRecords/{shiftRecordId}
```

### Clinical Risk Assessment

```txt
GET /riskAssessments
GET /riskAssessments/{riskAssessmentId}

GET   /clinicalAlerts
GET   /clinicalAlerts/{clinicalAlertId}
PATCH /clinicalAlerts/{clinicalAlertId}

GET /vitalSignReadings
GET /vitalSignReadings/{vitalSignReadingId}

GET   /vitalSignAnomalies
GET   /vitalSignAnomalies/{vitalSignAnomalyId}
PATCH /vitalSignAnomalies/{vitalSignAnomalyId}
```

### Staff Recovery

```txt
GET   /preventiveActions
GET   /preventiveActions/{preventiveActionId}
POST  /preventiveActions
PATCH /preventiveActions/{preventiveActionId}
```

### Audit Compliance

```txt
GET  /auditLogs
GET  /auditLogs/{auditLogId}
POST /auditLogs
```

---

## Email Invitations with Resend

Invitation emails are sent when calling:

```txt
POST /invitations/send
```

If Resend is disabled, the backend still creates the invitation but does not send a real email.

```txt
RESEND_ENABLED=false
```

If Resend is enabled, the backend sends the invitation email through Resend.

```txt
RESEND_ENABLED=true
RESEND_API_KEY=your_resend_api_key
RESEND_FROM_EMAIL=VitalWatch <invitations@notifications.vitalwatch.space>
```

The invitation link is generated using:

```txt
APP_PUBLIC_URL + /accept-invitation?token=...
```

For local frontend testing:

```txt
APP_PUBLIC_URL=http://localhost:4200
```

For production:

```txt
APP_PUBLIC_URL=https://app.vitalwatch.space
```

---

## Frontend Integration Notes

The Angular frontend should point to:

```txt
http://localhost:8080/api/v1
```

for local backend integration.

Invitation registration flow:

```txt
1. Admin sends invitation from backend.
2. Resend sends email.
3. User opens /accept-invitation?token=...
4. Frontend reads token.
5. Frontend calls GET /invitations/by-token/{token}.
6. Frontend calls POST /invitations/accept.
7. Backend creates user and returns authenticated session data.
```

Local frontend URL:

```txt
http://localhost:4200
```

Production frontend URL:

```txt
https://app.vitalwatch.space
```

Landing URL:

```txt
https://vitalwatch.space
```

---

## Recommended Validation Checklist

### Local IntelliJ

```txt
GET  /plans
POST /authentication/sign-in
GET  /organizations
GET  /users
GET  /invitations
GET  /specialties
GET  /workAreas
GET  /careTeams
GET  /teamMembers
GET  /shiftRecords
GET  /riskAssessments
GET  /clinicalAlerts
GET  /vitalSignReadings
GET  /vitalSignAnomalies
GET  /preventiveActions
GET  /auditLogs
```

### Docker

```txt
1. docker compose down
2. .\mvnw.cmd -DskipTests clean package
3. docker compose up --build
4. Open Swagger
5. Validate public endpoints
6. Sign in and authorize
7. Validate protected endpoints
8. docker compose down
```

---

## Git Workflow

Recommended workflow:

```bash
git switch develop
git pull origin develop
git switch -c feature/<feature-name>
```

After implementation:

```bash
git status
git add .
git commit -m "feat(scope): message"
git switch develop
git pull origin develop
git merge feature/<feature-name>
git push origin develop
```

Recommended branches used in the project:

```txt
feature/project-configuration
feature/iam-authentication
feature/subscriptions-billing
feature/iam-invitations
feature/iam-catalogs
feature/shift-coordination
feature/clinical-risk-assessment
feature/staff-recovery
feature/audit-compliance
```

---

## Author

VitalWatch Team

Course project developed for academic purposes.

---

## License

This project is licensed under the MIT License.
