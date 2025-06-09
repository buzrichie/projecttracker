# 🗂️ Project Tracker API

## 📌 Overview

Project Tracker API is a Spring Boot backend application that allows users to manage projects, tasks, and developers. 
It includes audit logging with MongoDB, supports pagination, sorting, caching, and advanced querying. 
Built for BuildMaster to track team productivity and task status effectively.

---

## ⚙️ Features

* ✅ CRUD operations for Projects, Tasks, and Developers
* ✅ Entity relationships using Spring Data JPA (`@OneToMany`, `@ManyToMany`, etc.)
* ✅ Audit logging to MongoDB for all create/update/delete actions
* ✅ Pagination and sorting support for large datasets
* ✅ Caching and transaction management for performance
* ✅ Advanced queries (overdue tasks, top developers, etc.)
* ✅ Swagger API Documentation
* ✅ Docker-Compose setup for MySQL & MongoDB

---

## 🏗️ Tech Stack

| Layer         | Technology             |
| ------------- | ---------------------- |
| Language      | Java 17+               |
| Framework     | Spring Boot            |
| ORM           | Spring Data JPA        |
| NoSQL Logging | MongoDB                |
| Database      | MySQL                  |
| Build Tool    | Maven                  |
| API Docs      | Swagger/OpenAPI        |
| Container     | Docker, Docker Compose |

---

## 🛠️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/project-tracker.git
cd project-tracker
```

### 2. Environment Configuration

Update `.env` or use default `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/project_db
spring.datasource.username=root
spring.datasource.password=root

spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=project_audit_db
```

### 3. Run Docker Compose

```bash
docker-compose up -d
```

### 4. Run the App

```bash
./mvnw spring-boot:run
```

---

## API Documentation

> Swagger UI available at:
> `http://localhost:8080/swagger-ui/index.html`

---

## REST API Endpoints

### Project

| Method | Endpoint         | Description            |
| ------ | ---------------- | ---------------------- |
| POST   | `/projects`      | Create a new project   |
| GET    | `/projects`      | Get paginated projects |
| PUT    | `/projects/{id}` | Update project by ID   |
| DELETE | `/projects/{id}` | Delete project by ID   |

### Task

| Method | Endpoint                            | Description                |
| ------ | ----------------------------------- | -------------------------- |
| POST   | `/tasks`                            | Create a new task          |
| PUT    | `/tasks/{id}/assign/{developerId}`  | Assign a task to developer |
| GET    | `/tasks/by-project/{projectId}`     | Get all tasks by project   |
| GET    | `/tasks/by-developer/{developerId}` | Get all tasks by developer |
| GET    | `/tasks/overdue`                    | Get overdue tasks          |

### Developer

| Method | Endpoint      | Description              |
| ------ | ------------- | ------------------------ |
| POST   | `/developers` | Create a new developer   |
| GET    | `/developers` | Get paginated developers |

### Audit Logs

| Method | Endpoint              | Description         |
| ------ | --------------------- | ------------------- |
| GET    | `/logs`               | All logs            |
| GET    | `/logs/by-entity/{e}` | Logs by entity type |
| GET    | `/logs/by-actor/{a}`  | Logs by actor name  |

---

## Advanced Queries

* `/tasks/overdue` — Tasks past due date and not completed
* `/developers/top` — Top 5 developers by task count
* `/projects/without-tasks` — Projects with no tasks
* `/tasks/status-summary` — Grouped task counts by status

---

## 👨‍💻 Author

**Richmond Kwame Nyarko**
[GitHub](https://github.com/buzrichie) 
