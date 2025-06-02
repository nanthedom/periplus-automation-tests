# Periplus Shopping Cart Test Automation

Automated test suite for testing shopping cart functionality on Periplus website using Selenium WebDriver with Java and TestNG.

## Overview

Tests the complete user journey:
1. Login to Periplus website
2. Search for products
3. Add products to cart
4. Verify cart contents

## Prerequisites

- **Java JDK 8+**
- **Apache Maven 3.6.0+**
- **Google Chrome browser**
- **Internet connection**

## Test Configuration

- **URL**: `https://www.periplus.com/`
- **Test Email**: `t2636923@gmail.com`
- **Test Password**: `periplusTest123`
- **Browser**: Chrome (maximized)
- **Wait Timeout**: 20 seconds

## How to Run

### 1. Clone the Repository
```bash
git clone https://github.com/nanthedom/periplus-automation-tests.git
cd periplus-automation-tests
```

### 2. Compile the Project
```bash
mvn clean compile
mvn test-compile
```

### 3. Run Tests
```bash
# Run all tests
mvn test

# Clean and run tests
mvn clean test
```

### 4. View Reports
Test reports are generated in `target/surefire-reports/`

## Dependencies

The project uses:
- Selenium WebDriver 4.15.0
- TestNG 7.8.0
- WebDriverManager 5.5.3

Dependencies are automatically managed by Maven.