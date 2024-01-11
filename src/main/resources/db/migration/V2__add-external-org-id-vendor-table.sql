ALTER TABLE expense_manager.vendor
ADD COLUMN externalorgid INT NULL,
ADD COLUMN country varchar(255) NULL,
MODIFY zip varchar(15) NULL;
CREATE INDEX externalorgid_idx on expense_manager.vendor(externalorgid);