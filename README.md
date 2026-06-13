# Listings App

## Author
Adam Filinger

## Compilation, Build, and Run Instructions

This project uses Java and Maven. To build and run the application, follow these steps:

1. **Prerequisites:**
   - Java Development Kit (JDK) 23 or later.
   - Apache Maven.

2. **Build the project:**
   Open a terminal in the project's root directory and run the following command to compile the source code and package it into a JAR file:
   ```bash
   mvn clean package
   ```

3. **Run the application:**
   After a successful build, you can run the application using the following command:
   ```bash
   java -jar target/listingsApp-1.0-SNAPSHOT.jar 
   ```
   *(Note: The exact JAR file name may vary depending on the project version defined in `pom.xml`)*

## Database
- **Name:** H2 Database Engine
- **Version:** 2.2.224 (or as specified in `pom.xml`)

## List of Mistakes
- Bilingual setup in datatabase model

## List of Differences to Initial Requirements
- When adding a listing, users can't choose to display the listing to only business users