
drop user if exists 'flyway_admin'@'%';
drop user if exists 'em_rw_user'@'%';


create user 'flyway_admin'@'%' identified by 'flyway';
create user 'em_rw_user'@'%' identified by 'adminUser';
-- Creating the user with the specified password
-- Assigning the role to the user
GRANT all privileges on expense_manager.* TO 'em_rw_user'@'%';
GRANT all privileges on expense_manager.* TO 'flyway_admin'@'%';




