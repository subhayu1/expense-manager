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
    paymentapplicationstatus int not null COMMENT '1=partially applied ,2=fully applied ,3=unapplied'
);

create table expense
(
    id            bigint auto_increment
        primary key,
    vendorid      bigint         not null,
    externalinvoicenumber varchar(255)   null,
    vendorinvoicenumber varchar(255)   null,
    integrationid varchar(255)   null,
    externalorgid int null,
    totalamount   decimal(38, 2) not null,
    amountdue     decimal(38, 2) not null,
    paymentamount decimal(38, 2) null,
    invoicedate  datetime(6)    null,

    duedate       datetime(6)    null,
    description   varchar(255)   null,
    createddate   timestamp      null,
    updateddate   timestamp      null,
    paymentstatus int            not null comment '1=partially paid ,2=fully paid ,3=unpaid 4=unknown'
);

create table billpayment_expense
(
    billpaymentid bigint not null references billpayment (id),
    expenseid     bigint not null references expense (id)
);
ALTER TABLE vendor ADD INDEX idx_vendor_externalorgid (externalorgid);
ALTER TABLE vendor ADD INDEX idx_vendor_integrationid (integrationId);