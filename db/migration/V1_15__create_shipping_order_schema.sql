CREATE TABLE shipping_order(
	batch_id varchar(50) NOT NULL PRIMARY KEY,
	shipping_vendor varchar(200) NOT  NULL,
	shipping_vendor_email varchar(150),
	description varchar(300),
	order_date datetime,
	status varchar(50)
) GO


CREATE TABLE shipping_order_items(
	batch_id varchar(50) NOT NULL,
	item_id varchar(200) NOT  NULL,
	unit_price numeric(19,2),
	quantity numeric (19,2),
	product_name varchar(250),
	seller_name varchar(250),
	page_link varchar(250),
	image_link varchar(250),
	rate varchar(250),
	created_date datetime
) GO


ALTER TABLE [shipping_order_items] ADD CONSTRAINT [orderItems] FOREIGN KEY ([batch_id]) REFERENCES [shipping_order] ([batch_id]) ON DELETE NO ACTION ON UPDATE CASCADE
go


ALTER TABLE [shipping_order_items] ADD CONSTRAINT [PKKeys] PRIMARY KEY (batch_id, item_id)

GO