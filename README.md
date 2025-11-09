🏦 ATM Simulator System
📘 Overview

The ATM Simulator System is a desktop-based banking application built using Core Java, Swing (GUI), JDBC, and MySQL.
It allows users to perform basic ATM operations such as depositing, withdrawing, and checking account balance, with improved security, error handling, and database design.

⚙️ Tech Stack

Programming Language: Java (JDK 8+)

GUI Framework: Java Swing / AWT

Database: MySQL

Database Connectivity: JDBC

Architecture: Layered (UI → Service → DAO)

IDE Used: IntelliJ IDEA / Eclipse

🚀 Features

✅ User login authentication using card number and PIN
✅ Deposit, Withdraw, and Balance Enquiry functionalities
✅ Persistent transaction storage using MySQL
✅ Exception handling for invalid input and database errors
✅ Efficient balance tracking using account_balance table
✅ SQL Injection prevention with PreparedStatement
✅ Clean modular design following OOP and MVC principles

🔒 Security & Improvements

This version includes key improvements and refactoring for security and performance:

🔐 Security

Replaced all string-concatenated SQL queries with PreparedStatements to prevent SQL Injection.

Removed all hard-coded database credentials from the Conn.java file.

Sanitized all user inputs (PIN, amount, etc.) before processing.

⚙️ Error Handling

Added user-friendly error messages for invalid input and failed transactions.

Enhanced connection error handling in Conn.java.

Implemented validation checks for withdrawal limits and balance availability.

🧩 Code Quality & Refactoring

Fixed class naming and spelling (BalanceEquiry.java → BalanceEnquiry.java).

Introduced account_balance table to improve transaction performance.

Added better code comments and method separation for readability.

Organized files into logical structure (UI, DAO, Database Connection).

🛠️ Setup Instructions

Database Setup

Install and start MySQL.

Create a database named bankmanagementsystem.

Run the following SQL commands:

CREATE TABLE login (
    cardno VARCHAR(20) PRIMARY KEY,
    pin VARCHAR(10)
);

CREATE TABLE bank (
    pin VARCHAR(10),
    date VARCHAR(50),
    mode VARCHAR(20),
    amount VARCHAR(20)
);

CREATE TABLE account_balance (
    pin VARCHAR(10) PRIMARY KEY,
    balance INT
);


Database Configuration

Open Conn.java in src/ASimulatorSystem/.

Replace the placeholders:

String username = "YOUR_USERNAME";
String password = "YOUR_PASSWORD";


with your MySQL credentials.

Run the Application

Compile and execute the Login.java file.

Log in using your test credentials and perform ATM transactions.

🧠 Learning Highlights

This project demonstrates:

Real-world use of Core Java and JDBC

GUI design using Java Swing

Database design & connectivity with MySQL

Input validation, exception handling, and OOP concepts

Best practices for small-scale financial applications

📈 Future Enhancements

Add a Change PIN feature.

Implement transaction timestamps and printable mini-statements.

Add JUnit testing for business logic validation.

Integrate email/SMS notifications using JavaMail API.

👨‍💻 Author

Jainil Ulhas Patil
📍 Pune, India
🔗 LinkedIn

💻 GitHub
