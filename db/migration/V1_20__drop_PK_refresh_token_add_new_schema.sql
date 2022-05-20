drop table refresh_token;

GO

CREATE TABLE refresh_token(
	user_id numeric(19)  NOT  NULL,
	token varchar(50) NOT NULL,
	expiryDate datetime NOT NULL
)
GO


ALTER TABLE [refresh_token] ADD CONSTRAINT [user_refresh_token] FOREIGN KEY ([user_id]) REFERENCES [users] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION

go

ALTER TABLE [refresh_token] ADD CONSTRAINT [PKIdUserID] PRIMARY KEY (user_id)
