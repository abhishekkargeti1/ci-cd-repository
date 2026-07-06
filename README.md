# 🚀 Authentication Service with OAuth2 & Automated CI/CD Pipeline

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Container-blue)
![GitHub Actions](https://img.shields.io/badge/GitHub-Actions-black)
![AWS EC2](https://img.shields.io/badge/AWS-EC2-orange)
![License](https://img.shields.io/badge/License-MIT-green)

## 📖 Overview

This project demonstrates a **production-ready Authentication & Authorization Service** developed using **Spring Boot**, **Spring Security**, **OAuth2**, and **JWT**. Along with the backend service, the project includes a complete **CI/CD pipeline** using **GitHub Actions**, **Docker**, **Docker Hub**, and **AWS EC2** to automate the entire deployment lifecycle.

The goal of this project is to showcase modern backend development along with DevOps practices by enabling automatic build, containerization, and deployment whenever new code is pushed to the repository.

---

# 📌 Project Objectives

- Build a secure Authentication Server for microservices.
- Implement OAuth2 authentication.
- Generate JWT Access Tokens and Refresh Tokens.
- Secure REST APIs using Spring Security.
- Containerize the application using Docker.
- Automate build and deployment using GitHub Actions.
- Deploy the application automatically on AWS EC2.
- Store sensitive credentials securely using GitHub Secrets.

---

# ✨ Features

## Authentication Features

- User Registration
- Secure User Login
- OAuth2 Authentication
- JWT Access Token Generation
- JWT Refresh Token Generation
- Role-Based Authorization (RBAC)
- Password Encryption using BCrypt
- Stateless Authentication
- Protected REST APIs
- Token Validation
- Refresh Token Mechanism
- Secure Environment Variable Configuration

---

## DevOps Features

- Continuous Integration (CI)
- Continuous Deployment (CD)
- Automated Maven Build
- Docker Image Creation
- Docker Hub Integration
- AWS EC2 Deployment
- GitHub Actions Workflow
- SSH-based Remote Deployment
- Automatic Container Replacement
- Secure Secret Management

---

# 🛠 Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- OAuth2
- JWT
- REST APIs

## Database

- MySQL

## Build Tool

- Maven

## DevOps

- Docker
- Docker Hub
- GitHub Actions
- AWS EC2
- Ubuntu Linux
- SSH

---

# 📂 Project Structure

```
Authentication-Service
│
├── .github
│   └── workflows
│       └── deploy.yml
│
├── src
│   ├── main
│   │   ├── java
│   │   ├── resources
│   │   └── ...
│   └── test
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# 🔐 Authentication Flow

```
Client
   │
   │ Login Request
   ▼
Authentication API
   │
   ▼
Validate Credentials
   │
   ▼
Generate JWT Access Token
Generate Refresh Token
   │
   ▼
Return Tokens
   │
   ▼
Client Uses Access Token
   │
   ▼
Protected APIs
```

---

# 🏗 CI/CD Architecture

```
                Developer

                    │

             Push Code to GitHub

                    │

                    ▼

             GitHub Repository

                    │

                    ▼

             GitHub Actions

        ┌──────────────────────┐
        │ Checkout Source Code │
        ├──────────────────────┤
        │ Setup Java           │
        ├──────────────────────┤
        │ Maven Build          │
        ├──────────────────────┤
        │ Build Docker Image   │
        ├──────────────────────┤
        │ Push to Docker Hub   │
        └──────────────────────┘

                    │

                    ▼

             AWS EC2 Server

        ┌──────────────────────┐
        │ Pull Latest Image    │
        ├──────────────────────┤
        │ Stop Old Container   │
        ├──────────────────────┤
        │ Remove Container     │
        ├──────────────────────┤
        │ Start New Container  │
        └──────────────────────┘

                    │

                    ▼

          Updated Application Live
```

---

# ⚙️ CI/CD Workflow

Whenever code is pushed to the **main** branch, GitHub Actions automatically executes the following steps:

### 1️⃣ Checkout Repository

Downloads the latest source code.

---

### 2️⃣ Setup Java

Installs Java 21 required for the project.

---

### 3️⃣ Build Application

Uses Maven to compile and package the Spring Boot application.

```bash
mvn clean package
```

---

### 4️⃣ Login to Docker Hub

Authenticates securely using GitHub Secrets.

---

### 5️⃣ Build Docker Image

Creates a Docker image of the Spring Boot application.

```bash
docker build -t username/auth-service .
```

---

### 6️⃣ Push Docker Image

Pushes the latest image to Docker Hub.

```bash
docker push username/auth-service
```

---

### 7️⃣ Connect to AWS EC2

GitHub Actions establishes an SSH connection with the EC2 instance.

---

### 8️⃣ Pull Latest Image

```bash
docker pull username/auth-service
```

---

### 9️⃣ Stop Existing Container

```bash
docker stop auth-service
```

---

### 🔟 Remove Old Container

```bash
docker rm auth-service
```

---

### 1️⃣1️⃣ Deploy New Container

```bash
docker run -d \
-p 8080:8080 \
--name auth-service \
username/auth-service
```

The latest version of the application is now live.

---

# 🔑 GitHub Secrets

The following secrets are configured for secure deployment.

| Secret Name | Description |
|-------------|-------------|
| DOCKER_USERNAME | Docker Hub Username |
| DOCKER_PASSWORD | Docker Hub Password / Access Token |
| EC2_HOST | AWS EC2 Public IP |
| EC2_USER | SSH User |
| EC2_SSH_KEY | Private SSH Key |
| MYSQL_URL | Database URL |
| MYSQL_USERNAME | Database Username |
| MYSQL_PASSWORD | Database Password |
| JWT_SECRET | JWT Secret |

---

# 🐳 Docker Commands

## Build Image

```bash
docker build -t auth-service .
```

## Run Container

```bash
docker run -d -p 8080:8080 --name auth-service auth-service
```

## Stop Container

```bash
docker stop auth-service
```

## Remove Container

```bash
docker rm auth-service
```

---

# ☁️ AWS EC2 Deployment

The application is deployed on an Ubuntu-based AWS EC2 instance.

Deployment is fully automated through GitHub Actions.

Deployment process:

- SSH into EC2
- Pull latest Docker image
- Stop existing container
- Remove old container
- Run latest container
- Pass environment variables
- Application becomes live automatically

No manual deployment steps are required after code is pushed.

---

# 🔒 Security Best Practices

- OAuth2 Authentication
- JWT Access & Refresh Tokens
- BCrypt Password Encryption
- Environment Variables
- GitHub Secrets
- Docker Container Isolation
- Spring Security
- Stateless Authentication

---

# 🚀 Future Enhancements

- Kubernetes Deployment
- Helm Charts
- SonarQube Integration
- Unit Testing Pipeline
- Integration Testing
- Redis Token Cache
- Jenkins Pipeline
- Nginx Reverse Proxy
- SSL using Let's Encrypt
- Monitoring using Prometheus
- Grafana Dashboard
- Blue-Green Deployment
- Rolling Deployment

---

# 🎯 Learning Outcomes

This project demonstrates practical knowledge of:

- Spring Boot
- Spring Security
- OAuth2
- JWT Authentication
- REST API Development
- Docker
- GitHub Actions
- Docker Hub
- AWS EC2
- Linux Administration
- SSH Automation
- CI/CD Pipeline Design
- Secure Credential Management
- Cloud Deployment
- DevOps Best Practices

---

# 👨‍💻 Author

## Abhishek Kargeti

**Java Full Stack Developer**

Experienced in:

- Spring Boot
- Spring MVC
- Spring Security
- Hibernate
- MySQL
- Docker
- GitHub Actions
- AWS EC2
- CI/CD
- REST APIs
- Microservices

GitHub:
https://github.com/abhishekkargeti1

LinkedIn:
https://www.linkedin.com/in/abhishekkargeti/

---

# ⭐ Show Your Support

If you found this project useful, please consider giving it a **⭐ Star** on GitHub.

It motivates me to continue building and sharing more Java, Spring Boot, Cloud, and DevOps projects.

Happy Coding! 🚀
