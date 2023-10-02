create table exchange_day (
    id int not null AUTO_INCREMENT PRIMARY KEY,
    name text not null,
    swishnumber text not null
);

create table seller (
    id int not null AUTO_INCREMENT PRIMARY KEY,
    name text not null,
    swishnumber varchar(256) not null,
    exchange_day_id int not null,
    FOREIGN KEY (exchange_day_id) REFERENCES exchange_day(id)
);

create table buy (
    id int not null AUTO_INCREMENT PRIMARY KEY,
    exchange_day_id int,
    FOREIGN KEY (exchange_day_id) REFERENCES exchange_day(id)
);

create table buy_row (
    id int not null AUTO_INCREMENT PRIMARY KEY,
    buy_id int not null,
    seller_id int not null,
    price decimal not null,
    FOREIGN KEY (buy_id) REFERENCES buy(id),
    FOREIGN KEY (seller_id) REFERENCES seller(id)
);
