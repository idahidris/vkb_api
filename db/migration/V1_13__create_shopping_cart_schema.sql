CREATE TABLE shopping_cart (
	id varchar(50) NOT NULL PRIMARY KEY,
	product_name varchar(120)  NOT NULL,
	seller_name varchar(120)  NOT NULL,
	quantity numeric (19,2)  DEFAULT 0,
	unit_price numeric(19,2)  DEFAULT 0,
	rate numeric(19,2) DEFAULT 1,
	page_link varchar(300) ,
	image_link varchar(300),
	created_date timestamp
)
GO



