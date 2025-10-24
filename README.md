# 🧾 AI Ticket Manager System

An intelligent customer support ticket management system built with **Spring Boot**, **MySQL**, and **Docker**, featuring **AI-powered ticket classification** using Hugging Face’s zero-shot classification model.

---

## 🚀 Features
- User authentication with role-based access (Customer, Agent, Manager)
- Customers can create and view support tickets
- AI service automatically classifies and prioritizes tickets based on content
- Prevents customers from submitting new tickets if a **High Priority** one is unresolved
- Agents can update ticket statuses and filter by category or priority
- Managers can view and assign tickets manually
- Fully containerized setup using **Docker Compose**

---

## 🧠 AI Integration
- Uses **Hugging Face Inference API** with a zero-shot classifier model (`facebook/bart-large-mnli`)
- The model predicts the **category** (e.g., Billing, Software Bug, Network Issue) and assigns a **priority level** based on confidence

---

## ⚙️ Tech Stack
- **Backend:** Spring Boot (Java 21)
- **Database:** MySQL 8.0 (Dockerized)
- **AI Integration:** Hugging Face API (Zero-Shot Classification)
- **Containerization:** Docker & Docker Compose
- **Build Tool:** Maven

---

## 🐳 Running Locally
### Prerequisites
- Docker Desktop installed
- Hugging Face API Token (optional)

