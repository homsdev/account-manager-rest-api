--Insert dummy account data
INSERT INTO cli_account(balance, alias, type)
VALUES (70000.00, 'Main Checking Account', 'SAVINGS');

INSERT INTO cli_account(balance, alias, type)
VALUES (120000.00, 'Savings Account', 'CHECKING');

INSERT INTO cli_account(balance, alias, type)
VALUES (24786.75, 'Nu Account', 'CHECKING');

INSERT INTO cli_category(category_id, category_name)
VALUES (1, 'Games');

INSERT INTO cli_category(category_id, category_name)
VALUES (2, 'Transportation');

INSERT INTO cli_category(category_id, category_name)
VALUES (3, 'Salary');

INSERT INTO cli_category(category_id, category_name)
VALUES (4, 'Groceries');

INSERT INTO cli_category(category_id, category_name)
VALUES (5, 'Services');

INSERT INTO cli_transaction(amount, type, date, alias,
                            transaction_account, transaction_category)
VALUES (1500.00, 'EXPENSE', '2026-03-16', 'Groceries', 1, 4);

INSERT INTO cli_transaction(amount, type, date, alias,
                            transaction_account, transaction_category)
VALUES (1500.00, 'EXPENSE', '2026-03-20', 'Groceries', 1, 4);

INSERT INTO cli_transaction(amount, type, date, alias,
                            transaction_account, transaction_category)
VALUES (1500.00, 'EXPENSE', '2026-03-25', 'Groceries', 1, 4);