# Spring-Security
Spring Security with AWS S3 Integration
This project demonstrates the use of Spring Security with role-based access control, combined with AWS S3 for file storage in a Spring Boot application.

Features
JWT-based authentication

AWS S3 integration for file upload/download

Secured and public REST endpoints

Prerequisites
Java 17 or above

AWS account with S3 bucket

Git (for cloning this repo)

Getting Started
1. Clone the Repository
    `git clone https://github.com/tejasgarud75/Spring-Security.git`
   `cd Spring-Security`
2. Configure AWS Credentials

3. Set Application Properties
   Update the following fields in application.properties:

   aws.s3.accessKey=test
   aws.s3.secretKey=test
   aws.s3.region=ap-south-1
   aws.s3.bucketName=test

Spring Security configurations if needed

The app will start on http://localhost:8085 by default.

Authentication and Token Usage
The application already includes a predefined user:

Username: admin
Password: admin

Use the following curl command to retrieve a JWT access token:

curl -X GET "http://localhost:8080/authenticate?userName=admin&password=admin"
This will return a JWT token which must be included in the Authorization header as a Bearer token for accessing secured APIs.

