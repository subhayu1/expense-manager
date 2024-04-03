create table user
(
    id       bigint auto_increment primary key,
    password varchar(255),
    role     varchar(255),
    username varchar(255) unique
);

create table vendor
(
    id         bigint auto_increment primary key,
    name       varchar(255) not null,
    vendornumber varchar(30)  not null unique,
    externalorgid int null,
    country varchar(2),
    vendortype int          comment '1=individual,2=company',
    address1   varchar(255),
    address2   varchar(255),
    city       varchar(255),
    state      varchar(20),
    zip        varchar(10),
    phone      varchar(10),
    email      varchar(255),
    createddate TIMESTAMP,
    updateddate TIMESTAMP,
    blocked varchar(20),
    integrationid varchar(36) unique
);

create table billpayment
(
    id                bigint auto_increment primary key,
    vendorid          bigint         not null references vendor (id),
    paymentmethod     int   comment '1=check,2=credit-card,3=cash,4=ACH,5=other',
    paymentreference  varchar(255),
    paymentdate       timestamp,
    createddate       timestamp,
    paymentamount decimal(38,2),
    paymentapplicationstatus int not null COMMENT '1=partially applied ,2=fully applied ,3=unapplied',
    tosync BOOLEAN NOT NULL DEFAULT TRUE,
    integrationid VARCHAR(36) NULL,
    appaymentid INT not NULL references appayment(id),
    expenseid INT NOT NULL references expense(id)


);

create table expense
(
    id            bigint auto_increment
        primary key,
    vendorid      bigint         not null,
    externalinvoicenumber varchar(255)   null,
    vendorinvoicenumber varchar(255)   null,
    integrationid varchar(255)   null unique ,
    externalorgid int null,
    totalamount   decimal(38, 2) not null,
    amountdue     decimal(38, 2) not null,
    paymentamount decimal(38, 2) null,
    invoicedate  datetime(6)    not null ,
    duedate       datetime(6)     not null,
    description   varchar(255)   null,
    createddate   timestamp      null,
    updateddate   timestamp      null,
    paymentstatus int            not null comment '1=partially paid ,2=fully paid ,3=unpaid 4=unknown',
    appaymentid INT  NULL DEFAULT 0
);

CREATE TABLE appayment (
                           id INTEGER PRIMARY KEY AUTO_INCREMENT,
                           paymentamount DECIMAL(10,2) NOT NULL,
                           paymentdate DATE NOT NULL,
                           paymentmethod INTEGER NOT NULL, -- Removed (255) as it's not applicable
                           paymentreference VARCHAR(255) NOT NULL,
                           externalorgid INTEGER ,
                           createddate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE appayment ADD INDEX idx_appayment_externalorgid (externalorgid);
ALTER TABLE vendor ADD INDEX idx_vendor_externalorgid (externalorgid);
ALTER TABLE vendor ADD INDEX idx_vendor_integrationid (integrationId);