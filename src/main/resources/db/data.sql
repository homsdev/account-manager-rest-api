--Insert dummy account data
INSERT INTO cli_account(account_id,balance,alias)
VALUES('e63e7a68-9e5e-45ab-a833-5dec938f08a8',70000.00,'Main Checking Account');

INSERT INTO cli_account(account_id,balance,alias)
VALUES('644cf9d5-c148-4bb5-bdcb-2c2c9725c200',120000.00,'Savings Account');

INSERT INTO cli_category(category_id,category_name)
VALUES('cat-games','Games');

INSERT INTO cli_category(category_id,category_name)
VALUES('cat-transportation','Transportation');

INSERT INTO cli_category(category_id,category_name)
VALUES('cat-salary','Salary');

INSERT INTO cli_category(category_id,category_name)
VALUES('cat-groceries','Groceries');

INSERT INTO cli_category(category_id,category_name)
VALUES('cat-services','Services');

INSERT INTO cli_transaction(transaction_id,transaction_amount,transaction_type,transaction_date,transaction_alias,transaction_account)
VALUES('574bea1c-521d-4f57-b555-e255449eef28',1500.00,'EXPENSE','2025-07-16','Groceries','e63e7a68-9e5e-45ab-a833-5dec938f08a8');

INSERT INTO cli_transaction(transaction_id,transaction_amount,transaction_type,transaction_date,transaction_alias,transaction_account)
VALUES('09d02618-eb5f-402a-835b-838b796da903',450.65,'EXPENSE','2025-07-01','Netflix','e63e7a68-9e5e-45ab-a833-5dec938f08a8');

INSERT INTO cli_transaction(transaction_id,transaction_amount,transaction_type,transaction_date,transaction_alias,transaction_account)
VALUES('749f6a3d-6c71-49d2-b6d4-2579fa2d9943',649.99,'EXPENSE','2025-08-16','Phone','e63e7a68-9e5e-45ab-a833-5dec938f08a8');