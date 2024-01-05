create table user
(
    id       bigint auto_increment
        primary key,
    password varchar(255) null,
    role     varchar(255) null,
    username varchar(255) null,
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);

create table vendor
(
    state      varchar(2)   null,
    vendortype int          null,
    zip        int          null,
    id         bigint auto_increment
        primary key,
    phone      varchar(10)  null,
    externalid varchar(30)  not null,
    address1   varchar(255) null,
    address2   varchar(255) null,
    city       varchar(255) null,
    email      varchar(255) null,
    name       varchar(255) null,
    constraint UK_ly8anka6nkirsxjhwg06jvy6e
        unique (externalid)
);

create table billpayment
(
    payment_amount    decimal(38, 2) null,
    id                bigint auto_increment
        primary key,
    vendorid          bigint         null,
    payment_method    varchar(255)   null,
    payment_reference varchar(255)   null,
    constraint FK1485novi0k3735wp6rsrg7spv
        foreign key (vendorid) references vendor (id)
);

create table expense
(
    amountdue     decimal(38, 2) null,
    paymentamount decimal(38, 2) null,
    totalamount   decimal(38, 2) null,
    duedate       datetime(6)    null,
    id            bigint auto_increment
        primary key,
    vendorid      bigint         null,
    description   varchar(255)   null,
    constraint FKcrk6vkvsix98rpwrkuij9itin
        foreign key (vendorid) references vendor (id)
);

create table billpayment_expense
(
    billpaymentid bigint not null,
    expenseid     bigint not null,
    constraint FKlp2xevaxatlv2hvnlwynssjwy
        foreign key (expenseid) references expense (id),
    constraint FKqeosptqx6vyygn8pla6vwiqhy
        foreign key (billpaymentid) references billpayment (id)
);

