# enterprise-ledger-system
The enterprise ledger system is a backend financial tracking application built using java springboot and postgreSQL.
It is designed to record, manage, and track financial transactikns in a structured and reliable manner. The system provides secure and scalable solution for maintaining debit and credit entries, generating balances, and ensuring financial data integrity.
# Purpose and Functionality 
The leger system is designed to provide a structured and reliable way of recording financial transactiobs.Its primary purpose is to maintain accurate records of debit and credit entries while ensuring data consistency and traceability .
At a high level, the system allows users to.
1. Create and manage ledger accounts
2. Record transactions linked to specific accounts
3. Automatically calculate balances based on debit and credit entries
4. Retrieve transactions history for audit and reporting.

The application follows a layered architecture built with Spring Boot, where REST APIs handle client requests , business logic processes transaction rules and postgreSQL securely stores financial data. Each transactikn is persisted using database level integrity constraints to ensure accuracy and prevent inconsistencies.
The system can serve as a foundational financial engine for applications such as wallet systems, accounting platforms, payment integrations or internal financial tracking tools.

# Getting Started 
Prerequisites 
Before running the application, ensure you have the following installed:
1. Java 17 or later
2. Maven or Gradle
3. PostgreSQL
4. git

Database Setup
CREATE DATABASE ledger_db;
Update your application.yml or application.Properties 
spring.datasource.url=jdbc:postgresql://localhost:5432/ledger_db
spring.datasource.username= your username
spring.datasource.password=your password 
spring.jpa.hibernate.ddl-auto= update

Running the application
Clone the repository 
git clone https//github.com/your-username/enterprise-ledger-system.git
cd enterprise-ledger-system


Run using Maven
mvn spring-boot:run 
Or build and run:
mvn clean install 
java -jar target/enterprise-ledger-system.jar

the application will start on:
http://localhost:8080 

#Usage 
Below are examples of how to interact with the enterprise-ledger systemonce the application is running locally
curl http://localhost:8080
 expected output:
 {
 "status":"Ledger system api is running"
 }

 Create a ledger Account

 curl -X POST http://localhost:8080/api/account \
 - H "Content-Type: application/json"\
 - d '
 "accountName":"Cash Account",
 "accountType":"ASSET"

 '
 Expected response

 {
 "id":1,
 "accountName":"Cash Account",
 "accountType":"ASSET",
 "balance":0.00
 }

 Record a transaction 

 curl -X POST http://localhost:8080/api/transactions\
 -H "Content-Type:application/json"
 -d '{
 "accountId":1,
 "type":"DEBIT",
 "amount":500.00,
 "description":"Initial depost*
 }'

 Expected Response:
 {
 "id":1,
 "accountId":1,
 "type":"DEBIT",
 "description":"Initial deposit",
 }

 Check account Balance

 curl http://localhost:8080/api/accounts/1

 Expected Response:

  {
 "id":1,
 "accountName":"Cash Account",
 "accountType":"ASSET",
 "balance":500.00,
 }

 Contact Information 
 **Author:** Haile Kesekwa
 **GitHub:** https://github.com/hailekesekwa
 **Email:** juliustelta@gmail 
 
