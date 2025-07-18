# 🔐 SpringSecurityWithRefreshToken

This project demonstrates how to implement **JWT Authentication with Refresh Tokens** using **Spring Security** and **Spring Boot**. It includes secure user login, access token generation, refresh token flow, and token validation with role-based access control.

---

## 🚀 Features

- User authentication with **JWT access and refresh tokens**
- Secure login and token issuance
- Refresh token endpoint to reissue access tokens
- Token expiry and validation
- Role-based authorization (e.g., USER, ADMIN)
- Stateless authentication with Spring Security
- Error handling for invalid or expired tokens

---

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **JWT (io.jsonwebtoken / jjwt)**
- **PostgreSQL / MySQL** (for user & token persistence)
- **JPA / Hibernate**
- **Lombok**, **MapStruct** (optional)
- **Maven / Gradle**

---

## 📁 Project Structure

SpringSecurityWithRefreshToken/
├── config/ # Security and JWT configuration
├── controller/ # Authentication & secured endpoints
├── dto/ # Request/response payloads
├── entity/ # User and token entities
├── repository/ # JPA Repositories
├── service/ # Auth and user services
├── security/ # JWT filters, providers, utils
└── exception/ # Global exception handling
