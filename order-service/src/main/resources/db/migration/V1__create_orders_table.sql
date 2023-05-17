create table if not exists orders(
id int not null auto_increment primary key,
product_id int,
quantity int,
customer_name varchar(64) not null
    );

