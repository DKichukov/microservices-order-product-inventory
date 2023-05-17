create table if not exists products(
     id int not null auto_increment primary key,
           name varchar(64) not null,
    description varchar(500),
        price double
    );