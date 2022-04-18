CREATE TABLE sales (
	id varchar(50) NOT NULL PRIMARY KEY,
	store_name varchar(120)  NOT NULL,
	item_name varchar(120)  NOT NULL,
	quantity varchar(20)  NOT NULL,
	unit_price varchar(20)  NOT NULL,
	total_price varchar(20)  NOT NULL,
	quantity_sold varchar(20)  NOT NULL,
	item_description varchar(200)  NOT NULL,
	manufactured_date varchar(50)  NOT NULL,
	expiry_date varchar(50) NOT NULL,
	customer_ref varchar(120)  NOT NULL,
	sales_date varchar(50)  NOT NULL,
	item_id varchar(120)  NOT NULL,
	batch_id varchar(120)  NOT NULL

) GO
