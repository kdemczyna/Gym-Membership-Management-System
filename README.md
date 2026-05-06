# Gym Membership Management System

A REST API for managing gym memberships, built with Java, Spring Boot and H2 in-memory database.  
Created as part of **SII - Letnia Akademia Talentów 2026**.

---

## Tech Stack

- Java 17
- Spring Boot 4.0.6
- Spring Data JPA / Hibernate
- H2 In-Memory Database
- Maven
- Lombok

---

## Features

- Manage multiple gyms, each with a unique name and phone number
- Create membership plans per gym (BASIC, PREMIUM, GROUP) with price and currency support
- Register members to plans with automatic capacity enforcement
- Cancel and reactivate memberships
- Revenue report grouped by gym and currency
- Input validation
- Global exception handling

---

## How to Build and Run

**Requirements:** Java 17+, Maven

```bash
# Clone the repository
git clone https://github.com/kdemczyna/Gym-Membership-Management-System.git
cd gym-management

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The app starts on `http://localhost:8080`

---

## H2 Database Console

To inspect the database while the app is running:

1. Open `http://localhost:8080/h2-console`
2. Set JDBC URL to `jdbc:h2:mem:gymdb`
3. Username: `sa`, Password: *(leave empty)*
4. Click Connect

---

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/gyms` | Create a gym |
| GET | `/api/gyms` | List all gyms |
| POST | `/api/gyms/{gymId}/plans` | Create a membership plan |
| GET | `/api/gyms/{gymId}/plans` | List plans for a gym |
| POST | `/api/plans/{planId}/members` | Register a member |
| GET | `/api/members` | List all members |
| PATCH | `/api/members/{memberId}/cancel` | Cancel a membership |
| PATCH | `/api/members/{memberId}/reactivate` | Reactivate a membership |
| GET | `/api/revenue` | Monthly revenue report |

---

## Sample Requests

### 1. Create a Gym

```http
POST http://localhost:8080/api/gyms
Content-Type: application/json

{
  "name": "FitLife Center",
  "address": "ul. Sportowa 1, Warszawa",
  "phone": "+48100200300"
}
```

Response `201 Created`:
```json
{
  "id": 1,
  "name": "FitLife Center",
  "address": "ul. Sportowa 1, Warszawa",
  "phone": "+48100200300"
}
```

---

### 2. List All Gyms

```http
GET http://localhost:8080/api/gyms
```

Response `200 OK`:
```json
[
  {
    "id": 1,
    "name": "FitLife Center",
    "address": "ul. Sportowa 1, Warszawa",
    "phone": "+48100200300"
  }
]
```

---

### 3. Create a Membership Plan

```http
POST http://localhost:8080/api/gyms/1/plans
Content-Type: application/json

{
  "name": "Basic Monthly",
  "type": "BASIC",
  "durationMonths": 1,
  "maxMembers": 2,
  "monthlyPrice": 49.99,
  "currency": "EUR"
}
```

Response `201 Created`:
```json
{
  "id": 1,
  "gymId": 1,
  "gymName": "FitLife Center",
  "name": "Basic Monthly",
  "type": "BASIC",
  "durationMonths": 1,
  "maxMembers": 2,
  "prices": {
    "EUR": 49.99
  }
}
```

---

### 4. List Plans for a Gym

```http
GET http://localhost:8080/api/gyms/1/plans
```

---

### 5. Register a Member

```http
POST http://localhost:8080/api/plans/1/members
Content-Type: application/json

{
  "name": "Jan",
  "surname": "Kowalski",
  "email": "jan.kowalski@email.com",
  "address": "ul. Kwiatowa 5, Warszawa"
}
```

Response `201 Created`:
```json
{
  "id": 1,
  "fullName": "Jan Kowalski",
  "email": "jan.kowalski@email.com",
  "address": "ul. Kwiatowa 5, Warszawa",
  "membershipStartDate": "2026-05-02",
  "status": "ACTIVE",
  "planName": "Basic Monthly",
  "gymName": "FitLife Center"
}
```

---

### 6. List All Members

```http
GET http://localhost:8080/api/members
```

---

### 7. Cancel a Membership

```http
PATCH http://localhost:8080/api/members/1/cancel
```

Response `200 OK`:
```json
{
  "id": 1,
  "fullName": "Jan Kowalski",
  "status": "CANCELLED",
  ...
}
```

---

### 8. Reactivate a Membership

```http
PATCH http://localhost:8080/api/members/1/reactivate
```

---

### 9. Revenue Report

```http
GET http://localhost:8080/api/revenue
```

Response `200 OK`:
```json
[
  { "gymName": "FitLife Center", "amount": 49.99, "currency": "EUR" },
  { "gymName": "Iron Gym", "amount": 1024.00, "currency": "PLN" }
]
```

---

## Error Responses

All errors return JSON with message:

```json
{ "error": "Membership plan is full" }
{ "error": "Gym not found" }
{ "name": "Name is required" }
{ "error": "invalid value" }

```

Common HTTP status codes used:
- `400 Bad Request` — validation failed (empty fields, invalid email, wrong enum value)
- `404 Not Found` — gym, plan or member doesn't exist
- `409 Conflict` — duplicate name/email, plan full, membership already cancelled

---

## Edge Cases Handled

- Plan capacity enforced — cannot register beyond `maxMembers` active members
- Cancelled members do not count toward plan capacity
- Gym names and phone numbers must be unique
- Member emails must be unique across the system
- Phone number validated to international format (+48100200300)
- Currency stored as ISO code (EUR, PLN, GBP) — always uppercased
- Prices stored as `BigDecimal`- no floating point errors
- Membership start date set automatically on registration and never changed
- New members always start with ACTIVE status
