    CREATE DATABASE IF NOT EXISTS expense_manager;
    USE expense_manager;

    CREATE USER IF NOT EXISTS 'flyway_admin'@'%' IDENTIFIED BY 'flyway';
    GRANT ALL PRIVILEGES ON expense_manager.* TO 'flyway_admin'@'%';

    CREATE USER IF NOT EXISTS 'em_rw_user'@'%' IDENTIFIED BY 'adminUser';
    GRANT ALL PRIVILEGES ON expense_manager.* TO 'em_rw_user'@'%';