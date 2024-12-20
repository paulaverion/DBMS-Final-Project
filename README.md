# Carbon Footprint Calculator

## Overview
The Carbon Footprint Calculator is a Java-based application designed to assist users in estimating and managing their carbon footprint. Users can log various activities such as electricity usage, transportation, air travel, and waste generation, and calculate the resulting environmental impact. The application features secure authentication and integrates with a MySQL database to store user and activity data.

## Features
- **User Registration and Login:** Securely register and authenticate users.
- **Activity Logging:** Record activities contributing to carbon emissions.
- **Detailed Reports:** View individual entries and calculate total carbon footprint.
- **Database Storage:** Stores user data and activity logs in a MySQL database for persistence.

## How It Works
1. **User Registration and Authentication:** Users must create an account and log in to access the application.
2. **Activity Input:** Users can input data related to electricity usage, travel, and waste.
3. **Carbon Footprint Calculation:** The system calculates the carbon footprint based on predefined formulas.
4. **Data Management:** All entries are stored securely in a MySQL database for future reference.

## Technologies Used
- **Programming Language:** Java
- **Database:** MySQL
- **External Libraries:** MySQL JDBC Driver for database connectivity

## Getting Started
1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. **Set Up the Database:**
   - Install MySQL.
   - Use the following schema to create the required database and tables:
     ```sql
     CREATE DATABASE IF NOT EXISTS tourist_db;

     USE tourist_db;

     CREATE TABLE IF NOT EXISTS users (
         id INT PRIMARY KEY AUTO_INCREMENT,
         username VARCHAR(50) NOT NULL UNIQUE,
         password VARCHAR(50) NOT NULL
     );

     CREATE TABLE IF NOT EXISTS footprint_entries (
         id INT PRIMARY KEY AUTO_INCREMENT,
         user_id INT NOT NULL,
         type VARCHAR(50) NOT NULL,
         amount DOUBLE NOT NULL,
         carbon_footprint DOUBLE NOT NULL,
         FOREIGN KEY (user_id) REFERENCES users(id)
     );

     CREATE TABLE IF NOT EXISTS carbon_footprint (
         id INT AUTO_INCREMENT PRIMARY KEY,
         user_id INT UNIQUE NOT NULL,
         totalCarbonFootprint DOUBLE NOT NULL,
         FOREIGN KEY (user_id) REFERENCES users(id)
     );
     ```

3. **Configure the Application:**
   - Update the database credentials in the `Main` class:
     ```java
     private static final String DB_URL = "jdbc:mysql://localhost:3306/tourist_db";
     private static final String DB_USER = "your_username";
     private static final String DB_PASSWORD = "your_password";
     ```

4. **Compile and Run the Application:**
   ```bash
   javac -cp .:mysql-connector-java-<version>.jar Main.java
   java -cp .:mysql-connector-java-<version>.jar Main
   ```

## Usage Instructions
- **Register:** Create a new user account.
- **Log In:** Access the system using your credentials.
- **Add Activities:** Input details for activities like electricity usage, vehicle travel, air travel, and waste.
- **View Results:** Display individual and total carbon footprint calculations.
- **Log Out:** Exit the system securely.
