
# Money Transfer REST API 

This repository contains a simple REST API for money transfer between accounts. The API is implemented using Spring Boot and Java, and it uses a relational database (PostgreSQL) to store account information and transactions.

## Features

The API provides the following endpoints:

1. **Create an Account:** `POST /accounts` - Creates a new account with a starting balance.

2. **Retrieve Account Information:** `GET /accounts/{id}` - Retrieves the account information for a given account ID.

3. **Transfer Money:** `POST /transfers` - Transfers money from one account to another.

## Prerequisites

Before running the API, you need to have the following installed on your system:

- Java Development Kit (JDK) 11 or higher
- Apache Maven
- MySQL database

## Getting Started

Follow the steps below to build and run the API:

1. Clone this repository to your local machine:

```bash
git clone https://github.com/ortupik/money-transfer-api.git
cd money-transfer-api
```

2. Set up the MySQL database:

   - Create a new database in MySQL, and note down the database URL, username, and password.
   - Update the database configuration in `src/main/resources/application.properties`.

3. Build the project using Maven:

```bash
mvn clean install
```

4. Run the API:

```bash
java -jar target/money-transfer-api-1.0.0.jar
```

The API will start running on `http://localhost:8081`.

## API Endpoints

### 1. Create an Account

- **Endpoint:** `POST /accounts`
- **Request Body:**
  ```json
  {
    "accountNumber": "your_account_number",
    "balance": 1000.0
  }
  ```
- **Response:**
  ```json
  {
    "id": 1,
    "accountNumber": "your_account_number",
    "balance": 1000.0
  }
  ```

### 2. Retrieve Account Information

- **Endpoint:** `GET /accounts/{id}`
- **Response:**
  ```json
  {
    "id": 1,
    "accountNumber": "your_account_number",
    "balance": 1000.0
  }
  ```

### 3. Transfer Money

- **Endpoint:** `POST /transfers`
- **Request Body:**
  ```json
  {
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "amount": 500.0
  }
  ```
- **Response:**
  ```json
  {
    "id": 1,
    "sourceAccountId": 1,
    "destinationAccountId": 2,
    "amount": 500.0
  }
  ```

## API Constraints

- An account can't have a negative balance.
- The transfer amount must be greater than zero.
- If the source account doesn't have enough funds, the transfer will fail.

## Security

The API is secured against common web vulnerabilities:

- **Cross-Origin Resource Sharing (CORS) Security:** CORS is enabled to allow specific domains to access the API. Replace `*` in the CORS configuration with specific origins that are allowed to access the API.

- **Cross-Site Scripting (XSS) Prevention:** Thymeleaf's built-in expression syntax (`th:text`, `th:utext`, etc.) is used to prevent XSS attacks when rendering user-generated content.

- **SQL Injection Prevention:** The API leverages Spring Data JPA, which automatically handles parameterized queries, preventing SQL injection attacks.

## Monitoring with Grafana and Prometheus

The API includes monitoring using Grafana and Prometheus:

- **Grafana Dashboard:** You can access the Grafana dashboard at `http://localhost:3000` (default Grafana URL). Log in using the default credentials (admin/admin) and create visualizations of the metrics exposed by the API.

- **Prometheus Configuration:** The API exports metrics to Prometheus at `http://localhost:8080/actuator/prometheus`. Prometheus scrapes the API's metrics at regular intervals.

## Logging

The API uses SLF4J for logging, and Logback serves as the logging implementation. Log messages are printed to the console with the date, time, log level, logger name, and the log message.

## Docker Deployment

To deploy the API using Docker, you can use the provided Dockerfile to build a Docker image and run the container. Use the following commands:

```bash
docker build -t money-transfer-api .
docker run -d -p 8080:8080 money-transfer-api
```

## Kubernetes Deployment

To deploy the API using Kubernetes, use the provided Kubernetes Deployment YAML file to deploy the application. Use the following command:

```bash
kubectl apply -f deployment.yaml
```

## Jenkins Pipelines

To set up Jenkins pipelines for continuous integration and deployment, you'll need a Jenkins server running and properly configured. Create a Jenkins pipeline job using the provided Jenkinsfile. The pipeline automates the build, Docker image creation, and deployment to Kubernetes.

