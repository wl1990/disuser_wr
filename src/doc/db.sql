drop  table if exists user;
create table user(
id int primary key auto_increment,
name varchar(64),
birthday timestamp default current_timestamp);