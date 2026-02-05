# unsent-api
Backend API for Unsent, an online diary platform enabling users to write, share, and react to personal entries with privacy and anonymity at its core.

## 🚀 Local Setup Guide

This project can be run locally in **two ways**:

1. Using **Docker for PostgreSQL** (recommended)
2. Running the **entire application locally without Docker**

---
#### Clone the repository
~~~~bash
git clone https://github.com/JatinBhargava/unsent-api.git
~~~~

#### Create environment file
~~~~bash
cp .env.example .env
~~~~

#### Configure application-docker
~~~~bash
spring.datasource.username=postgres
spring.datasource.password=your_password
~~~~

#### Run the application

## Run Application Locally with PostgreSQL via Docker

This approach keeps your local machine clean while ensuring a consistent and reproducible database setup.

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Git

### Steps

~~~~bash
docker compose up -d to run in docker

docker compose down - to stop the images
~~~~



## 📦 Maven Packaging Strategy (Optimized)

In this setup, Jenkins performs only fast validation using Maven (`compile`, `test`, `verify`) and does **not** execute the final packaging step.  
The actual application JAR is built inside the Docker **multi-stage build**, ensuring a clean and reproducible artifact on every run.

This approach:
- Eliminates duplicate Maven executions
- Reduces overall pipeline execution time
- Ensures environment consistency between build and runtime
- Avoids “works on my machine” issues by standardizing builds inside Docker