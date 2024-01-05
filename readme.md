# Expense Manager

Expense Manager is a Java-based project for managing expenses. It provides functionality for tracking and managing expenses, vendors, and bill payments.

## Features

- Track and manage expenses
- Manage vendors and bill payments
- Generate reports and summaries
- Integration with MySQL database
- Docker compose support for setting up MySQL database

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven build tool
- MySQL database

## Getting Started

1. Clone the repository:
     using https:

    ```shell
    git clone https://github.com/subhayu1/expense-manager.git
    ```
    using ssh:

    ```shell
    git clone git@github.com:subhayu1/expense-manager.git
    ```
2. Navigate to the project directory:

    ```shell
    cd expense-manager
    ```
3. Create sql users and database byrunning the follwing script:

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
7. Using Postman:
    
        ```
        postman collection is available in the src/main/resources folder
        ```
        
        ```
## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).