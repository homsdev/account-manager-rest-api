CREATE TABLE IF NOT EXISTS cli_account(
    account_id VARCHAR(60) NOT NULL PRIMARY KEY,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (balance >= 0),
    alias VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS cli_transaction(
    transaction_id VARCHAR(60) NOT NULL PRIMARY KEY,
    transaction_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (transaction_amount > 0),
    transaction_type VARCHAR(10) NOT NULL CHECK (transaction_type IN ('EXPENSE', 'INCOME')),
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transaction_alias VARCHAR(60),
    transaction_account VARCHAR(60) NOT NULL,
    FOREIGN KEY (transaction_account) REFERENCES cli_account(account_id)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS cli_category(
    category_id VARCHAR(60) NOT NULL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);