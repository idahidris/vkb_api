CREATE TABLE goods (
	id varchar(50) NOT NULL PRIMARY KEY,
	name varchar(120)  NOT NULL,
	quantity varchar(20)  NOT NULL,
	unit_price varchar(20)  NOT NULL,
	quantity_sold varchar(20)  NOT NULL,
	description varchar(200)  NOT NULL,
	manufactured_date varchar(50)  NOT NULL,
	expiry_date varchar(50) NOT NULL

) GO