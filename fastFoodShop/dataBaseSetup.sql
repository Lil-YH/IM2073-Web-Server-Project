create database if not exists fastfoodshop;
 
use fastfoodshop;
 
drop table if exists food;
create table food (
  id     int,
  foodType  varchar(50),
  foodItem  varchar(50),
  calories int,
  price  float,
  qty    int,
  primary key (id));
 
insert into food values (1001, 'Burger','Cheese Burger', 450, 5.50, 5);
insert into food values (1001, 'Burger','Fish Burger', 450, 5.50, 5);
insert into food values (1001, 'Burger','Chicken Burger', 450, 5.50, 5);
insert into food values (1002, 'Fries','Fries Large', 500, 3.00, 5);
insert into food values (1003, 'Fries','Fries Small', 230, 2.00, 5);
insert into food values (1004, 'Drink','Coke Large', 290, 2.50, 5);
insert into food values (1005, 'Drink','Coke Small', 140, 1.90, 5);
 
select * from food;