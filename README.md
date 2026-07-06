# 🚀 CI/CD Pipeline for Spring Boot Authentication Service

A complete CI/CD pipeline for a Spring Boot Authentication Service using **GitHub Actions**, **Docker**, **Docker Hub**, and **AWS EC2**.

This project demonstrates how to automate the build, containerization, and deployment of a Spring Boot application whenever changes are pushed to the `main` branch.

---

## 📌 Project Overview

The pipeline automatically performs the following tasks:

- Clone the latest source code from GitHub
- Build the application using Maven
- Create a Docker image
- Push the image to Docker Hub
- Connect to an AWS EC2 instance using SSH
- Pull the latest Docker image
- Stop and remove the existing container
- Deploy the latest version automatically

This enables **Continuous Integration** and **Continuous Deployment (CI/CD)** with minimal manual intervention.

---

## 🏗️ Architecture

```
Developer
     │
     ▼
GitHub Repository
     │
     ▼
GitHub Actions
     │
     ├── Build Maven Project
     ├── Create Docker Image
     ├── Push Image to Docker Hub
     │
     ▼
AWS EC2 Server
     │
     ├── Pull Latest Image
     ├── Stop Existing Container
     ├── Remove Old Container
     └── Start New Container
```

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Maven
- Docker
- Docker Hub
- GitHub Actions
- AWS EC2
- Linux
- SSH

---

## 📂 Repository Structure

```
.
├── .github/
│   └── workflows/
│       └── deploy.yml
├── src/
├── pom.xml
├── Dockerfile
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## ⚙️ GitHub Actions Workflow

The workflow is triggered automatically whenever code is pushed to the **main** branch.

### Workflow Steps

- Checkout Repository
- Setup Java
- Build Spring Boot Project
- Login to Docker Hub
- Build Docker Image
- Push Docker Image
- Connect to AWS EC2
- Pull Latest Image
- Stop Existing Container
- Remove Old Container
- Deploy Updated Container

---

## 🔐 GitHub Secrets

The following GitHub Secrets are required:

| Secret | Description |
|---------|-------------|
| DOCKER_USERNAME | Docker Hub Username |
| DOCKER_PASSWORD | Docker Hub Password/Access Token |
| EC2_HOST | AWS EC2 Public IP |
| EC2_USER | SSH Username (ubuntu/ec2-user) |
| EC2_SSH_KEY | Private SSH Key |
| MYSQL_URL | Database URL |
| MYSQL_USERNAME | Database Username |
| MYSQL_PASSWORD | Database Password |
| JWT_SECRET | JWT Secret Key |

> **Note:** Never commit secrets directly into the repository. Use GitHub Secrets for secure credential management.

---

## 🐳 Docker

The application is containerized using Docker.

### Build Image

```bash
docker build -t auth-service .
```

### Run Container

```bash
docker run -d \
-p 8080:8080 \
--name auth-service \
auth-service
```

---

## ☁️ AWS EC2 Deployment

The deployment process includes:

- SSH into AWS EC2
- Pull latest Docker image
- Stop running container
- Remove old container
- Start new container with latest image
- Pass environment variables securely

Deployment is fully automated through GitHub Actions.

---

## 🚀 CI/CD Flow

```
Code Push
      │
      ▼
GitHub Actions Trigger
      │
      ▼
Maven Build
      │
      ▼
Docker Image Build
      │
      ▼
Push to Docker Hub
      │
      ▼
SSH into AWS EC2
      │
      ▼
Pull Latest Docker Image
      │
      ▼
Deploy New Container
      │
      ▼
Application Updated
```

---

## 📈 Features

- Continuous Integration
- Continuous Deployment
- Dockerized Spring Boot Application
- Secure Secret Management
- Automated AWS EC2 Deployment
- Docker Hub Integration
- GitHub Actions Automation
- Production Deployment Ready

---

## 📚 Learning Objectives

This repository demonstrates practical implementation of:

- GitHub Actions
- CI/CD Pipelines
- Docker
- Docker Hub
- AWS EC2 Deployment
- SSH Automation
- Spring Boot Deployment
- Secure Environment Variable Management

---

## 📸 Future Enhancements

- Kubernetes Deployment
- Blue-Green Deployment
- Rolling Updates
- SonarQube Integration
- Unit Test Automation
- Integration Tests
- Slack Notifications
- Prometheus Monitoring
- Grafana Dashboard

---

## 👨‍💻 Author

**Abhishek Kargeti**

Java Full Stack Developer

GitHub: https://github.com/abhishekkargeti1

LinkedIn: https://www.linkedin.com/in/abhishekkargeti/

---

## ⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub.

It helps others discover the project and motivates further development.
