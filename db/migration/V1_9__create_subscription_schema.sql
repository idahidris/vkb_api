CREATE TABLE subscription(
	id varchar(50) NOT NULL PRIMARY KEY,
	customer_id varchar(20) NOT  NULL,
	service_type varchar(50),
	service_title varchar(300),
	document_link varchar(200),
	description varchar(250),
	price numeric(19,2),
	paid_amount numeric(19,2),
	paid_amount_date datetime,
	last_payment_reference varchar(120),
	subscription_date datetime,
	expected_delivery_date datetime,
	actual_delivery_date datetime,
	status varchar(50)
) GO





