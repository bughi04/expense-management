# Expense Management System

[![Java](https://img.shields.io/badge/Java-22-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JavaFX](https://img.shields.io/badge/JavaFX-22.0.1-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.13.0-red.svg)](https://maven.apache.org/)
[![SQLite](https://img.shields.io/badge/SQLite-3.47.2.0-green.svg)](https://www.sqlite.org/)

A comprehensive expense management system with **dual interfaces**: a JavaFX desktop GUI and a RESTful API backend. Features **AI-powered currency predictions** using custom-built machine learning algorithms (linear regression) for 7-day exchange rate forecasting.

> **Note**: This project was designed and implemented by me, with selective use of AI tools for research, refactoring, and productivity - similar to modern IDE assistance.

## Key Features

### Core Functionality
- **Multi-Currency Support** - Track expenses in USD, EUR, GBP, JPY, AUD, and RON with automatic conversion via real-time exchange rate APIs
- **Category Management** - Create, organize, and delete expense categories with cascading options
- **Dual Date Tracking** - Separate transaction and expense date recording for accurate financial reporting
- **Financial Reports** - Comprehensive category breakdowns with spending percentages and totals
- **Database Persistence** - SQLite database for reliable local data storage

### AI/ML Component (Custom Implementation)
- **Currency Prediction System** - 7-day exchange rate forecasts using linear regression
- **Built from Scratch** - Custom ML implementation without external ML libraries
- **Historical Data Analysis** - Analyzes 30 days of historical exchange rate patterns
- **Trading Recommendations** - Actionable insights based on predicted USD strength/weakness
- **Asynchronous Processing** - Non-blocking UI updates using JavaFX Tasks

### REST API with Swagger Documentation
- **RESTful Endpoints** - Complete CRUD operations for expenses and categories
- **API Documentation** - Interactive Swagger UI for API exploration and testing
- **Currency Prediction API** - Programmatic access to ML predictions
- **JSON Responses** - Standardized response format with error handling

## Technical Stack

### Core Technologies
- **Language**: Java 22
- **Framework**: Spring Boot 3.2.0
- **UI Framework**: JavaFX 22.0.1
- **Database**: SQLite 3.47.2.0 (JDBC)
- **Build Tool**: Maven 3.13.0
- **API Documentation**: Swagger/OpenAPI 3.0

### Architecture & Design Patterns
- **MVC Pattern** - Clear separation of Model, View, and Controller layers
- **Service Layer** - Business logic encapsulation
- **DTO Pattern** - Data Transfer Objects for API communication
- **SOLID Principles** - Adherence to Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion

### Programming Paradigms
- **Object-Oriented Programming** - Inheritance, Polymorphism, Encapsulation, Abstraction
- **Functional Programming** - Streams API, Lambda expressions, method references
- **Concurrent Programming** - JavaFX Tasks for asynchronous operations

## Quick Start

### Prerequisites
- Java 22 or higher
- Maven 3.8 or higher

### Running the JavaFX Application
```bash
# Clone the repository
git clone https://github.com/bughi04/expense-management-ai.git
cd expense-management-ai

# Run the JavaFX GUI
mvn clean javafx:run
```

### Running the REST API Server
```bash
# Start the Spring Boot application
mvn spring-boot:run

# Access Swagger UI documentation
# Open browser to: http://localhost:8080/swagger-ui.html
```

The SQLite database (`expenses.db`) is automatically created on first launch.

## API Endpoints

### Expense Management
- `GET /api/expenses` - Retrieve all expenses
- `GET /api/expenses/category/{categoryName}` - Get expenses by category
- `POST /api/expenses` - Add new expense
- `DELETE /api/expenses/{id}` - Delete expense
- `GET /api/expenses/report` - Generate comprehensive report
- `GET /api/expenses/total` - Get total expenses

### Category Management
- `GET /api/expenses/categories` - List all categories
- `POST /api/expenses/categories` - Create category
- `DELETE /api/expenses/categories/{name}` - Delete category

### Currency Predictions (AI)
- `GET /api/predictions` - Get predictions for all currencies
- `GET /api/predictions/{currency}` - Get prediction for specific currency
- `GET /api/predictions/{currency}/historical` - Get 30-day historical data
- `GET /api/predictions/{currency}/future` - Get 7-day future predictions
- `GET /api/predictions/{currency}/change` - Get predicted percentage change
- `GET /api/predictions/supported` - List supported currencies

## Machine Learning Implementation

The prediction system demonstrates:
- **Linear Regression Algorithm** - Implemented from scratch using least squares method
- **Statistical Analysis** - Mean calculation, variance handling, and trend detection
- **Time Series Forecasting** - 7-day predictions based on 30-day historical patterns
- **Data Preprocessing** - Historical rate simulation with realistic market volatility
- **Model Evaluation** - Change percentage calculations and recommendation generation

### ML Algorithm Details
```java
// Custom Linear Regression Implementation
y = a + b*x
where:
- b = Σ((xi - x̄)(yi - ȳ)) / Σ((xi - x̄)²)
- a = ȳ - b*x̄
```

## Testing

Comprehensive unit and integration tests using:
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework for isolated unit tests
- **Test Coverage** - Service layer (ExpenseService, CurrencyPredictionService)

```bash
# Run all tests
mvn test
```

## Project Structure
```
expense-management-ai/
├── src/main/java/org/example/project/
│   ├── ExpenseManagerApp.java           # JavaFX application entry
│   ├── ExpenseManagementApplication.java # Spring Boot entry
│   ├── DatabaseManager.java             # Database operations
│   ├── CurrencyPredictor.java          # ML implementation
│   ├── controller/                      # REST controllers
│   ├── service/                         # Business logic
│   └── dto/                            # Data Transfer Objects
├── src/test/java/                      # Unit tests
└── pom.xml                             # Maven configuration
```

## OOP Concepts Demonstrated

1. **Inheritance** - Model classes with hierarchical relationships
2. **Polymorphism** - Interface implementations (Budgetable, TransactionCalculable)
3. **Encapsulation** - Private fields with public getters/setters
4. **Abstraction** - Service layer abstracting business logic

## SOLID Principles

1. **Single Responsibility** - Each class has one clear purpose (DatabaseManager, CurrencyPredictor, Services)
2. **Open/Closed** - Extensible through interfaces without modifying existing code
3. **Liskov Substitution** - Service implementations can be substituted with mocks
4. **Interface Segregation** - Focused interfaces (Budgetable, TransactionCalculable)
5. **Dependency Inversion** - Services depend on abstractions, not concrete implementations

## Skills Demonstrated

- Advanced Java programming (Java 22 features)
- Spring Boot framework integration
- RESTful API design and implementation
- API documentation with Swagger/OpenAPI
- JavaFX desktop application development
- SQL database design and management (SQLite)
- Machine learning algorithm implementation
- Unit testing with JUnit and Mockito
- Functional programming (Streams, Lambdas)
- Concurrent programming (JavaFX Tasks)
- Maven project management
- MVC architectural pattern
- SOLID principles
- DTO pattern implementation

## Future Enhancements

- Export reports to PDF/CSV formats
- Data visualization with interactive charts
- Budget alerts and notifications
- Cloud database synchronization
- User authentication and multi-user support
- Mobile companion app (Android/iOS)
- Historical data caching for faster predictions
- Additional ML models (ARIMA, Prophet)
