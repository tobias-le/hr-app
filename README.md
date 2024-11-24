# Timely Backend

## Overview

Timely Backend is a Spring Boot application designed to manage employee attendance, projects, and leave requests. It provides a comprehensive API for interacting with the system, including endpoints for managing employees, projects, teams, attendance records, and leave requests.

## Test Environment

The test environment for this project is hosted at [backend-test-7dda.up.railway.app](https://backend-test-7dda.up.railway.app).

## API Documentation

You can access the Swagger UI for the API documentation at [backend-test-7dda.up.railway.app/swagger-ui.html](https://backend-test-7dda.up.railway.app/swagger-ui.html). This provides a detailed view of all available API endpoints, their expected parameters, and responses.

## Key Features

- **Employee Management**: Create, update, and retrieve employee details.
- **Project Management**: Manage projects and assign employees to projects.
- **Team Management**: Organize employees into teams and manage team details.
- **Attendance Tracking**: Record and manage employee attendance data.
- **Leave Management**: Handle leave requests and track employee leave balances.

## Development

This project uses Maven for dependency management and build processes. The main dependencies include Spring Boot, Spring Data JPA, and PostgreSQL for database interactions.

### Building the Project

To build the project, run the following command in the root directory:

```bash
mvn clean install
```

### Running the Project Locally

To run the project locally, you can use the following command:

```bash
mvn spring-boot:run
```

## Testing

The application includes unit tests for services and integration tests for controllers. Tests are run using JUnit and Mockito.

### Running Tests

To execute tests, use:

```bash
mvn test
```

## Configuration

The application is configured through the `application.properties` file located in `src/main/resources`. This includes database connection settings, Hibernate configurations, and Swagger setup.

## Continuous Integration

The project is configured with GitHub Actions for continuous integration, which includes steps for building the project and running tests on push or pull requests to main branches.

## Contributing

Contributions to the project are welcome. Please ensure to follow the existing code style and include tests for new features or changes.
