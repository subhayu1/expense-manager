CREATE TABLE appayment (
                           id INTEGER PRIMARY KEY AUTO_INCREMENT,
                           paymentamount DECIMAL(10,2) NOT NULL,
                           paymentdate DATE NOT NULL,
                           paymentmethod INTEGER NOT NULL, -- Removed (255) as it's not applicable
                           paymentreference VARCHAR(255) NOT NULL,
                           externalorgid INTEGER ,
                           createddate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE appayment
    ADD INDEX idx_appayment_externalorgid (externalorgid);

