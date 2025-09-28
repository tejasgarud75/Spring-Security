# Inventory Management System - Technical Overview

## 📋 Executive Summary

A secure, cloud-native inventory management system built with Spring Boot that combines robust security, AWS cloud integration, and comprehensive inventory tracking with complete audit capabilities.

## 🎯 Core Purpose

### To provide enterprises with a secure, scalable solution for managing product inventory with real-time tracking, audit compliance, and cloud storage integration.

## 🏗️ System Architecture

Technology Stack

**Backend Framework** : Spring Boot 3.x

**Security**: Spring Security + JWT

**Database**:  PostgreSQL/MySQL

**Cloud Storage**: AWS S3

**Build Tool**: Maven

**Java Version**: 17+

## Security Architecture

Client → JWT Authentication → Spring Security → Role-based Access → Business Logic

## Inventory Flow

Product Creation → Stock Operations → Audit Logging → Monitoring/Alerts

🔑 Key Features

1. Security & Access Control
   JWT-based authentication system

Role-based endpoint protection

Automatic user context tracking

Secure credential management

2. Inventory Management
   
Product Lifecycle: Full CRUD operations

Stock Control: Real-time quantity tracking

Business Rules: Non-negative stock enforcement

Threshold Monitoring: Low stock alerts

3. Audit & Compliance
   Complete operation history

User-action correlation

Timestamped change tracking

Compliance-ready reporting

4. Cloud Integration
   AWS S3 file storage

Secure upload/download

Cloud-native ready architecture

## 📊 API Structure

Core Endpoints

`Authentication: /api/authenticate
Products:      /api/products
Stock:         /api/products/{id}/increase|decrease-stock
Monitoring:    /api/products/low-stock
Audit:         /api/products/{id}/audit-history`

## Data Models

Product: Core inventory items

StockAudit: Change history records

User: Authentication and authorization

🚀 Deployment & Operations
Development Setup
bash
## Quick start
git clone <repo>
mvn spring-boot:run

## **NOTE** 

The app will start on http://localhost:8085 by default.

Authentication and Token Usage
The application already includes a predefined user:

Username: admin
Password: admin

Use the following curl command to retrieve a JWT access token:

curl -X GET "http://localhost:8085/api/authenticate?userName=admin&password=admin"
This will return a JWT token which must be included in the Authorization header as a Bearer token for accessing secured APIs.


**Additionally, I have attached postman collection file .**

File name : POC-VERTO.postman_collection.json
