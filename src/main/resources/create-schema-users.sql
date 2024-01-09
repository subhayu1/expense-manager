CREATE DATABASE IF NOT EXISTS expense_manager;
USE expense_manager;

DELIMITER //
CREATE PROCEDURE CreateDatabaseAndUsers()
BEGIN
  DECLARE _exists INT;

  SELECT COUNT(*) INTO _exists FROM mysql.user WHERE user = 'flyway_admin';
  IF _exists = 0 THEN
    CREATE USER 'flyway_admin'@'%' IDENTIFIED BY 'flyway';
    GRANT ALL PRIVILEGES ON expense_manager.* TO 'flyway_admin'@'%';
  END IF;

  SELECT COUNT(*) INTO _exists FROM mysql.user WHERE user = 'em_rw_user';
  IF _exists = 0 THEN
    CREATE USER 'em_rw_user'@'%' IDENTIFIED BY 'adminUser';
    GRANT ALL PRIVILEGES ON expense_manager.* TO 'em_rw_user'@'%';
  END IF;

  FLUSH PRIVILEGES;
END//
DELIMITER ;
