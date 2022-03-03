create database if not exists fastfoodshop;
 
use fastfoodshop;
 
drop table if exists food;
create table food (
  id     int,
  foodItem  varchar(50),
  calories int,
  price  float,
  qty    int,
  primary key (id));
 
insert into food values (1001, 'Hamburger', 450, 5.50, 5);
insert into food values (1002, 'Fries Large', 500, 3.00, 5);
insert into food values (1003, 'Fries Small', 230, 2.00, 5);
insert into food values (1004, 'Coke Large', 290, 2.50, 5);
insert into food values (1005, 'Coke Small', 140, 1.90, 5);
 
select * from food;