ALTER TABLE expense
ADD COLUMN appaymentid INT NOT NULL DEFAULT 0;


ALTER TABLE billpayment
DROP COLUMN expenseid;