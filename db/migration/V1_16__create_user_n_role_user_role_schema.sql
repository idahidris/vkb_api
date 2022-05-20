CREATE TABLE users(
	id numeric(19) identity PRIMARY KEY,
	email varchar(50) NOT  NULL,
	password varchar(120) NOT NULL,
	username varchar(20) NOT NULL,
	created_date datetime
) GO


CREATE TABLE roles(
	id numeric(19) identity PRIMARY KEY NOT NULL,
	name varchar(20) NOT  NULL,
	created_date datetime
) GO


CREATE TABLE user_roles(
	user_id numeric(19) NOT NULL,
	role_id numeric(19) NOT  NULL,
	created_date datetime
) GO


ALTER TABLE [user_roles] ADD CONSTRAINT [user_roles_users] FOREIGN KEY ([user_id]) REFERENCES [users] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
go


ALTER TABLE [user_roles] ADD CONSTRAINT [user_roles_role] FOREIGN KEY ([role_id]) REFERENCES [roles] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
go


ALTER TABLE [user_roles] ADD CONSTRAINT [USERROLEPKKeys] PRIMARY KEY (user_id, role_id)

GO
