# Expense Manager

Expense Manager is a Java-based Spring-Boot project for managing expenses.
It provides functionality for tracking and managing expenses, vendors, and bill payments.

## Housekeeping
- The application creates a default admin user with username: admin and password: password at startup.
- `create-db-schema-and-users.sh` script creates the database schema and users required for the application to run.
- The application uses the default port 8080. If you want to change the port, you can do so by changing the `server.port` property in the `application.yml` file.

## Features

- Track and manage expenses
- Manage vendors and bill payments
- Generate reports and summaries
- Integration with MySQL database
- Docker compose support for setting up MySQL database
- Spring Security for authentication and authorization

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven build tool
- Docker engine (Docker Desktop-optional)

## Getting Started

1. Clone the repository using either HTTPS or SSH:

    ```shell
    # using HTTPS
    git clone https://github.com/subhayu1/expense-manager.git

    # using SSH
    git clone git@github.com:subhayu1/expense-manager.git
    ```

2. Navigate to the project directory:

    ```shell
    cd expense-manager
    ```

3. Create SQL users and database by running the following script:

    ```shell
    chmod +x create-db-schema-and-users.sh
    ./create-db-schema-and-users.sh
    ```

4. Build the project using Maven:

    ```shell
    mvn clean install
    ```

5. Run the application:

    ```shell
    mvn spring-boot:run
    ```

6. Access the application in your web browser:

    ```
    http://localhost:8080
    ```

## Testing functionality using Postman
- Import the postman collection from the `src/main/resources` folder.
- First, get the access token by sending a POST request to the following URL using basic auth with username: admin and password: password:

    ```
    http://localhost:8080/token
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
--TBD

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).