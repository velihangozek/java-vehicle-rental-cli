# Java Vehicle Rental CLI

A terminal-based vehicle-rental application implemented in Java 21 with layered architecture (DAO → Service → CLI) and PostgreSQL.
Admins can add/manage vehicles; customers can search, filter, paginate, rent and view rental history—with full business rules, transactional safety, and joined reporting.

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Getting Started](#getting-started)

   1. [Prerequisites](#prerequisites)
   2. [Clone & Build](#clone--build)
   3. [Database Setup](#database-setup)
   4. [Configuration](#configuration)
   5. [Running the CLI](#running-the-cli)
5. [Usage Example](#usage-example)
6. [Running Tests](#running-tests)
7. [Future Improvements](#future-improvements)

---

## Features

* **User Management**: register as CUSTOMER, login as CUSTOMER or ADMIN
* **Password Security**: SHA-256 hashing
* **Vehicle Catalog**: ADMIN can add cars, motorcycles, helicopters
* **Search & Filter**: free-text search by brand/model, filter by type
* **Pagination**: list vehicles in pages of configurable size
* **Rental Workflow**: only logged-in CUSTOMERS can rent
* **Business Rules**:

  * Corporate must rent ≥ 30 days total
  * Vehicles > 2 000 000 ₺ require renter > 30 years old + 10% deposit
  * Hourly, daily, weekly, monthly rates
* **Transaction Safety**: rental creation is atomic (JDBC transaction)
* **Reporting**:

  * Past rentals with joined vehicle info
  * Detailed confirmation including user & vehicle data

---

## Tech Stack

* **Language:** Java 21
* **Build:** Maven
* **Database:** PostgreSQL
* **Persistence:** JDBC (no ORM)
* **Testing:** JUnit 5

---

## Project Structure

```
java-vehicle-rental-cli
├── pom.xml
└── src
    ├── main
    │   ├── java/org/velihangozek/javarentalcli
    │   │   ├── dao
    │   │   │   ├── UserDao.java
    │   │   │   ├── VehicleDao.java
    │   │   │   └── RentalDao.java
    │   │   ├── dao/impl
    │   │   │   ├── UserDaoImpl.java
    │   │   │   ├── VehicleDaoImpl.java
    │   │   │   └── RentalDaoImpl.java
    │   │   ├── exception
    │   │   │   ├── VeloAuthenticationException.java
    │   │   │   ├── VeloBusinessRuleException.java
    │   │   │   └── VeloDataAccessException.java
    │   │   ├── model
    │   │   │   ├── User.java
    │   │   │   ├── Vehicle.java
    │   │   │   ├── Rental.java
    │   │   │   ├── RentalDetail.java
    │   │   │   └── RentalFullDetail.java
    │   │   ├── model/enums
    │   │   │   ├── VehicleType.java
    │   │   │   └── PeriodType.java
    │   │   ├── service
    │   │   │   ├── UserService.java
    │   │   │   ├── VehicleService.java
    │   │   │   └── RentalService.java
    │   │   ├── service/impl
    │   │   │   ├── UserServiceImpl.java
    │   │   │   ├── VehicleServiceImpl.java
    │   │   │   └── RentalServiceImpl.java
    │   │   ├── util
    │   │   │   ├── DBConnection.java
    │   │   │   └── PasswordUtil.java
    │   │   └── VeloRentalMain.java
    │   └── resources
    │       └── db
    │       │   └── velo_rental_backup.sql
    │       │
    │       └── application.properties
    └── test
        └── java/org/velihangozek/javarentalcli
            └── TransactionalityTest.java
```

---

## Getting Started

### Prerequisites

* Java 21 JDK
* Maven 4+
* PostgreSQL 17+

### Clone & Build

```bash
git clone https://github.com/velihangozek/java-vehicle-rental-cli.git
cd java-vehicle-rental-cli
mvn clean package
```

### Database Setup

1. **Create the database**

   ```bash
   psql -U postgres -c "CREATE DATABASE velo_rental;"
   ```
2. **Load schema & seed data**

   ```bash
   psql -U postgres -d velo_rental -f src/main/resources/db/velo_rental_backup.sql
   ```

### Configuration

Database settings live in `src/main/resources/application.properties`:

```properties
jdbc.url=jdbc:postgresql://localhost:5432/velo_rental
jdbc.username=postgres
jdbc.password=postgres
```

Adjust as needed.

### Running the CLI

```bash
mvn exec:java -Dexec.mainClass=org.velihangozek.javarentalcli.VeloRentalMain
```

Or, if you built a JAR:

```bash
java -jar target/java-vehicle-rental-cli.jar
```

---

## Usage Example

```text
=== Welcome to the Velo Rental System ===

1) Register
2) Login
0) Exit
> 2

Email: admin@velo.example
Password: admin123
Login OK

--- Admin Menu ---
1) Add vehicle
2) List vehicles
0) Logout
> 2

ID   Type         Brand           Model            Hourly    Daily   Weekly   Monthly    Purchase    Deposit
------------------------------------------------------------------------------------------------------------
 1   CAR          Toyota          Corolla           100.00   600.00  3000.00   10000.00     1500000          -
 2   HELICOPTER   Robinson        R44 Raven II     10000.00 60000.00 350000.00 1000000.00  30000000  3000000.00
...
```

---

## Running Tests

```bash
mvn test
```

* **TransactionalityTest** verifies that failed rentals roll back cleanly.

---

## Future Improvements

* Add a DI framework (e.g. Spring)
* Enhance input validation & user prompts
* Export reports (CSV/JSON) or add a summary dashboard
* Swap to JPA/Hibernate or reactive drivers

---