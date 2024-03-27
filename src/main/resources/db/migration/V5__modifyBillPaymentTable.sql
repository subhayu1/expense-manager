ALTER TABLE billpayment
    ADD COLUMN expenseid INT NOT NULL references expense(id),
    ADD COLUMN appaymentid INT NOT NULL references appayment(id);
