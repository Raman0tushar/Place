# PlaceMentor Backend API ⚙️

[![Java](https://img.shields.io/badge/Language-Java-orange.svg?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/Database-MongoDB%20Atlas-green.svg?logo=mongodb)](https://www.mongodb.com/atlas)
[![Render](https://img.shields.io/badge/Deployed%20on-Render-black?logo=render)](https://render.com/)

The core RESTful backend service for **PlaceMentor**, designed to handle user authentication, preparation metrics, and mock assessment data. Built with a scalable architecture to support high-performance client applications.

**🚀 Live DEPLOY:** `https://placementor-front-end.vercel.app/`
**🚀 Live API Base URL:** `https://place-d7ee.onrender.com/api`

---

## 🛠️ Tech Stack & Architecture

- **Core Framework:** Spring Boot (Java 17+)
- **Database:** MongoDB Atlas (Cloud NoSQL Database)
- **Data Access:** Spring Data MongoDB
- **Security:** Spring Security & JWT (JSON Web Tokens) for stateless authentication
- **Build Tool:** Maven
- **Hosting/Deployment:** Render

---

## 📋 Core Features

- **Secure Authentication:** Role-based access control and secure endpoints using JWT.
- **Data Persistence:** Seamless integration with MongoDB Atlas for flexible and scalable document storage.
- **RESTful API Design:** Clean, resource-oriented endpoints for client-side consumption.
- **Cloud Deployment:** Fully containerized and actively deployed via Render.

---

## ☁️ Deployment Environment

This backend is actively deployed and hosted on **Render**. 

You can interact with the live REST API without setting it up locally by pointing your frontend or API testing tools (like Postman or Insomnia) directly to the endpoints listed below.

---

## 🔗 Key API Endpoints (Live)

| Method | Endpoint | Full Live URL | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | `https://place-d7ee.onrender.com/api/auth/register` | No |
| `POST` | `/auth/login` | `https://place-d7ee.onrender.com/api/auth/login` | No |
| `POST` | `/interview/mock` | `https://place-d7ee.onrender.com/api/interview/mock` | Yes |
| `POST` | `/resume/analyze` | `https://place-d7ee.onrender.com/api/resume/analyze` | Yes |
| `GET` | `/auth/me` | `https://place-d7ee.onrender.com/api/auth/me` | Yes |

---

## ⚙️ Local Development Setup

If you wish to run the server locally for development or testing, follow these steps:

### Prerequisites
- **Java JDK 17** or higher
- **Maven** (3.8+)
- **MongoDB Atlas** Account & Cluster URI

### Steps to Run Locally

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/Raman0tushar/Place.git](https://github.com/Raman0tushar/Place.git)
   cd Place
