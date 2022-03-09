/*run in mysql client console using source command before using web interface*/

create database if not exists fastfoodshop;
 
use fastfoodshop;
 
drop table if exists order_records;
create table order_records (
  id int,
  qty_ordered int
);
drop table if exists cart;
create table cart (
  id int,
  qty_ordered int
);
drop table if exists food;
create table food (
  id     int,
  foodType  varchar(50),
  foodItem  varchar(50),
  calories int,
  price  float,
  qty    int,
  foodImage varchar(50),
  primary key (id));
 
insert into food values (1001, 'Burger','Cheese Burger', 450, 5.50, 0, 'cheeseburger.jpg');
insert into food values (1002, 'Burger','Fish Burger', 450, 5.50, 5, 'fishburger.jpg');
insert into food values (1003, 'Burger','Chicken Burger', 450, 5.50, 4, 'chickenburger.jpg');
insert into food values (1004, 'Fries','Fries Large', 500, 3.00, 3, 'fries.png');
insert into food values (1005, 'Fries','Fries Small', 230, 2.00, 2, 'friessmall.png');
insert into food values (1006, 'Drink','Coke Large', 290, 2.50, 6, 'cokelarge.png');
insert into food values (1007, 'Drink','Coke Small', 140, 1.90, 10, 'cokesmall.jpg');
 
select * from food;