# 🛒 ShopEase Backend

Production-oriented backend for ShopEase, a full-stack e-commerce application built using Java and Spring Boot.

The project started as a monolithic Spring Boot application and is currently being migrated toward a scalable microservices architecture.

---

# 🚀 Project Overview

ShopEase Backend provides REST APIs for:

- Product Management
- User Authentication
- Authorization
- Shopping Cart
- Wishlist
- Checkout
- Orders
- Payments
- Product Ratings
- Admin Management
- Inventory
- Search & Filtering

The backend uses JWT-based authentication and MySQL for persistent data storage.

---

# 🛠️ Tech Stack

## Backend

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- BCrypt
- Maven

## Database

- MySQL

## Payment

- Razorpay

## DevOps & Tools

- Docker
- Docker Compose
- Git
- GitHub
- GitHub Actions
- Eclipse
- Postman
- XAMPP

---

# 🏗️ Current Architecture

The project is currently transitioning from a monolithic architecture to microservices.

### Current Microservices

```text
ShopEase
│
├── Product Service      → 8081
├── User Service         → 8082
├── Cart Service         → 8083
├── Order Service        → 8084
├── Payment Service      → Coming Soon
└── API Gateway          → Coming Soon

The original monolithic backend is being kept during the migration to ensure that existing functionality remains safe and testable.

✅ Completed Backend Modules
Module 1 — Spring Boot Setup
Spring Boot Project
Maven Configuration
REST Controllers
Project Structure
Module 2 — Product Management
Product Entity
Product Repository
Product Service
Product Controller
MySQL Integration
Hibernate Mapping
CRUD APIs
Module 3 — Database
MySQL Database
Spring Data JPA
Hibernate
Schema Management
Database Seed Data
Module 4 — Exception Handling
Custom Exceptions
Global Exception Handler
@ControllerAdvice
Standard Error Responses
Module 5 — Validation
@Valid
@NotBlank
@NotNull
@Positive
@Min
@Max
Custom Validation Messages
Module 6 — DTO Layer
Request DTOs
Response DTOs
DTO Mapping
Entity Exposure Protection
Module 7 — Standard API Response
Generic ApiResponse<T>
Success Responses
Error Responses
Validation Error Responses
🔐 Authentication & Security
User Management
User Registration
Login
BCrypt Password Hashing
User Profile
Change Password
JWT Security
JWT Token Generation
JWT Authentication Filter
Token Validation
Protected APIs
Role-Based Authorization
Roles
USER
ADMIN
🛒 E-Commerce Features
Product
Product Details
Product Specifications
Similar Products
Product Search
Category Filtering
Brand Filtering
Price Filtering
Rating Filtering
Multiple Filters
Pagination
Sorting
Cart
Add to Cart
Update Quantity
Remove Item
View Cart
Persistent Cart
User-Specific Cart
Stock Validation
Wishlist
Add Wishlist Item
Remove Wishlist Item
View Wishlist
User-Specific Wishlist
Orders
Checkout
Shipping Address
Order Creation
Order Items
Order History
Order Details
User-Specific Orders
Order Status Management
Status Transition Validation
Product Rating
Buyer-Based Rating
Delivered-Order Validation
Add Rating
Update Rating
Product Rating APIs
💳 Payment System

Razorpay integration has been implemented and tested in the monolithic backend.

Features:

Razorpay Order Creation
Payment Verification
Signature Verification
Payment Status
Payment Failure Handling
Order-Payment Integration
Successful Payment Handling
Cart Handling

The Payment functionality will be migrated into a dedicated Payment Service during the microservices phase.

👨‍💼 Admin Features
Admin Dashboard
Product Management
User Management
Order Management
Order Status Management
Inventory Management
Low Stock Alerts
Out-of-Stock Alerts
Recent Orders
Product Image Upload
Feedback Moderation
🖼️ Image Upload
Multipart File Upload
Product Image Storage
UUID-Based File Names
Image URL Storage
Static Image Serving
Admin Image Management
📊 Search, Filtering & Pagination

Backend supports:

Product Name Search
Category Filtering
Brand Filtering
Minimum Price
Maximum Price
Minimum Rating
Multiple Filters
Sorting
Pagination
Search + Filtering
Filtering + Sorting
Filtering + Pagination
🐳 Docker

Docker environment implemented with:

Dockerfile
Docker Compose
Spring Boot Container
MySQL Container
Docker Networking
Environment Variables
Persistent MySQL Volume
Persistent Product Image Storage
🔄 CI/CD

GitHub Actions has been implemented for Continuous Integration.

Current setup includes:

Automated Build
Automated Tests
GitHub Actions Workflow

Continuous Deployment remains part of the future deployment phase.

🚀 Microservices Migration

Microservices migration is currently in progress.

Completed
Service	Port	Status
Product Service	8081	✅ Complete
User Service	8082	✅ Complete
Cart Service	8083	✅ Complete
Order Service	8084	✅ Complete
Remaining
Component	Status
Payment Service	🔄 Next
API Gateway	⬜ Pending
Service Communication	⬜ Pending
Event-Driven Architecture	⬜ Pending
Kafka / RabbitMQ	⬜ Pending
Integration & Testing	⬜ Pending
🗺️ Development Roadmap
Phase 1 — Core Backend
Spring Boot
REST APIs
MySQL
JPA / Hibernate
CRUD
DTOs
Validation
Exception Handling
Phase 2 — Security
Spring Security
JWT
BCrypt
Role-Based Authorization
Phase 3 — E-Commerce
Products
Cart
Wishlist
Checkout
Orders
Ratings
Phase 4 — Payments
Razorpay
Payment Verification
Order-Payment Integration
Phase 5 — Admin
Dashboard
Products
Users
Orders
Inventory
Phase 6 — Production Features
Logging
Pagination
Sorting
Search
Filtering
Image Upload
Phase 7 — DevOps
Docker
Docker Compose
CI/CD
Phase 8 — Microservices
Product Service
User Service
Cart Service
Order Service
Payment Service
API Gateway
Service Communication
Event-Driven Architecture
Kafka / RabbitMQ
Phase 9 — Advanced Backend
Concurrency & Multithreading
Race Condition Handling
Redis & Caching
Messaging
Distributed Systems
Resilience Patterns
Observability
Advanced Security
Database Transactions
Testing & Quality Engineering
System Design
Load Testing
Production Hardening
🎯 Current Status
Core Backend                 ✅
Authentication & Security    ✅
E-Commerce Features          ✅
Payments                     ✅
Admin Panel                  ✅
Search & Filtering           ✅
Image Upload                 ✅
Logging                      ✅
Docker                       ✅
CI/CD                        ✅

Microservices Migration      🔄 In Progress
Current Development Target

30.6 — Payment Service

After that:

API Gateway → Service Communication → Event-Driven Architecture → Kafka/RabbitMQ → Integration Testing

👨‍💻 Developer

Shitanshu Jha
