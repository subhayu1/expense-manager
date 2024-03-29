[![CircleCI](https://dl.circleci.com/status-badge/img/gh/subhayu1/expense-manager/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/subhayu1/expense-manager/tree/main)


# Expense Manager

Expense Manager is a Java-based Spring-Boot project for managing expenses.
It provides functionality for tracking and managing expenses, vendors, and bill payments.

## Housekeeping
- The application creates a default admin user with username: `admin` and password: `password` at startup.
- `create-db-schema-and-users.sh` script creates the database schema and users required for the application to run.
- The application uses the default port 8080. If you want to change the port, you can do so by changing the `server.port` property in the `application.yml` file.

## Features

- Track and manage expenses
- Manage vendors and bill payments
- Integration with MySQL database
- Docker compose support for setting up MySQL database
- Spring Security for authentication and authorization
- Uses Flyway for database migration

## Prerequisites

- Java Development Kit (JDK) 17 or higher
-  install Amazon Corretto 17 JDK from
   https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html 
   if needed (optional if you don't currently have JDK 17 installed)
- Docker engine with docker-compose
- make sure the following ports are available:
    - 8080 for the application
    - 3306 for MySQL database
- make sure JAVA_HOME is set to the JDK 17 or higher installation directory 
  if you plan to run it from the command line

## Getting Started
- Recommended build tool is `Gradle` using the Gradle wrapper(`./gradlew`).

1. Clone the repository using either HTTPS or SSH:

    ```shell
    # using HTTPS
    git clone https://github.com/subhayu1/expense-manager.git
    ```
    ```shell
    # using SSH
    git clone git@github.com:subhayu1/expense-manager.git
    ```

2. Navigate to the project directory and create the log file.

    ```shell
    cd expense-manager && touch logs/app.log
    ```
3. Create Private and Public Keys for JWT token generation and verification
    ```shell
      chmod -x ./generate-keys-for-auth.sh 
   ```
   ```shell
      bash -x ./generate-keys-for-auth.sh
    ```
4. create the database using docker-compose. this will create the database 
    and the required users by running the init.sql script at startup 
     ```shell:
      docker-compose up -d
    ```
5. run the Migration manually to create the tables if needed.
   However, Spring should auto-run the migration at startup
    ```shell
    ./gradlew flywayInfo
    ```
6. Build the project using Maven or Gradle(recommended)

    ```shell
    mvn clean install
    ```
    OR
    ```shell
    ./gradlew clean build
    ```

7. Run the application using Maven or Gradle(recommended). 
   Flyway will automatically create the database tables

    ```shell
    mvn spring-boot:run
    ```
    OR
    ```shell
    ./gradlew bootRun
    ```

8. Access the application in your web browser:

    ```
    http://localhost:8080
    ```

## Testing functionality using Postman
- Import the postman collection from the `src/main/resources` folder.
- Alternatively the following link can be used to access the collection:
    ```
  https://api.postman.com/collections/9714675-dac75db9-246b-4a8d-9021-f45d66a118a1?access_key=PMAT-01HKRC3JTDZ20ZS57ZBD3GXCH3
   ```
  
- First, get the access token by sending a POST request to the following URL using basic auth with username: admin and password: password:

    ```
    http://localhost:8080/auth/token
    ```

- Copy the access token from the response if you want to use Swagger UI.

## Testing functionality using Swagger UI
- Access the Swagger UI in your web browser:

    ```
    http://localhost:8080/swagger-ui.html
    ```

- Click on the Authorize button and enter the access token you got from the Postman response in the appropriate field.
- Call the APIs to test the functionality.

## Using Docker Image to run the application
-- Not implemented yet --

## Testing
- Unit tests are written using JUnit 5 and Mockito.
- Integration tests are written using JUnit 5 
 and RESTAssured and uses TestContainers to spin up test databases.
- To run the tests, run the following command:

    ```shell
    mvn clean test
    ```
    OR
    ```shell
    ./gradlew clean test
    ```
- To run integration test (only implemented with Gradle) run the following command:

    ```shell
    ./gradlew  testInteg
    ```
- To run database migration manually, run the following command:
 
   ```shell
    ./gradlew flywayMigrate
    ```
## CI Pipeline
- CircleCI is used for CI pipeline.
- The pipeline is configured to run the unit and integration tests and build the application.

## Database fields

### User Table

| Column   | Type         | Description |
|----------|--------------|-------------|
| id       | bigint       | A unique identifier for each user. It's a primary key and auto-increments with each new entry. |
| password | varchar(255) | The password for the user. |
| role     | varchar(255) | The role of the user. |
| username | varchar(255) | The username of the user. It's unique for each user. |

### Vendor Table

| Column       | Type         | Description |
|--------------|--------------|-------------|
| id           | bigint       | A unique identifier for each vendor. It's a primary key and auto-increments with each new entry. |
| name         | varchar(255) | The name of the vendor. It's a required field. |
| externalid   | varchar(30)  | An external identifier for the vendor. It's unique and required. |
| vendortype   | int          | The type of the vendor. 1 for individual, 2 for company. |
| address1     | varchar(255) | The first line of the vendor's address. |
| address2     | varchar(255) | The second line of the vendor's address. |
| city         | varchar(255) | The city of the vendor's address. |
| state        | varchar(2)   | The state of the vendor's address. |
| zip          | int          | The zip code of the vendor's address. |
| phone        | varchar(10)  | The phone number of the vendor. |
| email        | varchar(255) | The email of the vendor. |
| createddate  | TIMESTAMP    | The date when the vendor was created. |
| updateddate  | TIMESTAMP    | The date when the vendor was last updated. |

### Bill Payment Table

| Column                  | Type          | Description |
|-------------------------|---------------|-------------|
| id                      | bigint        | A unique identifier for each bill payment. It's a primary key and auto-increments with each new entry. |
| vendorid                | bigint        | The id of the vendor to whom the payment is made. It's a foreign key referencing the vendor table. |
| paymentmethod           | int           | The method of payment. 1 for check, 2 for credit card, 3 for cash, 4 for ACH, 5 for other. |
| paymentreference        | varchar(255)  | The reference for the payment. |
| paymentdate             | timestamp     | The date when the payment was made. |
| createddate             | timestamp     | The date when the payment was created. |
| paymentamount           | decimal(38,2) | The amount of the payment. |
| paymentapplicationstatus| int           | The status of the payment application. 1 for partially applied, 2 for fully applied, 3 for unapplied. |

### Expense Table

| Column       | Type          | Description |
|--------------|---------------|-------------|
| id           | bigint        | A unique identifier for each expense. It's a primary key and auto-increments with each new entry. |
| vendorid     | bigint        | The id of the vendor who is associated with the expense. It's a foreign key referencing the vendor table. |
| totalamount  | decimal(38,2) | The total amount of the expense. It's a required field. |
| amountdue    | decimal(38,2) | The amount due of the expense. It's a required field. |
| paymentamount| decimal(38,2) | The amount of the payment for the expense. |
| duedate      | datetime(6)   | The due date of the expense. |
| description  | varchar(255)  | The description of the expense. |
| createddate  | timestamp     | The date when the expense was created. |
| updateddate  | timestamp     | The date when the expense was last updated. |
| paymentstatus| int           | The status of the payment. 1 for partially paid, 2 for fully paid, 3 for unpaid. |

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
