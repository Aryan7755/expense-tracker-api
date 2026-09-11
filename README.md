# Smart Expense Tracker API 🚀

A production-ready RESTful API built with **Spring Boot 3**, **Java 17**, and **Spring Security** to manage personal finances, set category budgets, and export spending data.

## 🛠️ Tech Stack
- **Framework:** Spring Boot 3.x
- **Language:** Java 17
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Database:** H2 In-Memory
- **Documentation:** OpenAPI 3 / Swagger UI
- **Build Tool:** Maven

## 📋 Features
- **JWT Authentication:** Stateless user signup & login.
- **Expense CRUD & Filtering:** Filter by date range and category.
- **Budget Tracking:** Automated warning triggers when monthly category budgets are exceeded.
- **CSV Data Export:** Stream expense logs directly into CSV format.
- **Interactive Swagger Docs:** Live API testing UI.

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher
- Maven 3.8+

### Running the Application
```bash
# Clone repository
git clone https://github.com/Aryan7755/expense-tracker-api

# Run Spring Boot application
./mvnw spring-boot:run