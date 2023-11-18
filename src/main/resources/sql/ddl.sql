DROP TABLE IF EXISTS Bank CASCADE;
DROP TABLE IF EXISTS "User" CASCADE;
DROP TABLE IF EXISTS Account CASCADE;
DROP TABLE IF EXISTS "Transaction" CASCADE;

CREATE TABLE Bank (
    bank_id SERIAL PRIMARY KEY,
    bank_name VARCHAR(50) NOT NULL,
    bank_address VARCHAR(100) NOT NULL,
	deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE "User" (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    user_email VARCHAR(50) UNIQUE NOT NULL,
    user_phone VARCHAR(20) UNIQUE NOT NULL,
	deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE Account (
    account_id SERIAL PRIMARY KEY,
    account_number VARCHAR(24) UNIQUE NOT NULL,
    account_balance NUMERIC(10,2) NOT NULL CHECK (account_balance >= 0),
    account_type VARCHAR(10) NOT NULL CHECK (account_type IN ('savings', 'checking')),
    user_id INTEGER NOT NULL REFERENCES "User"(user_id),
    bank_id INTEGER NOT NULL REFERENCES Bank(bank_id),
	deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE "Transaction" (
    transaction_id SERIAL PRIMARY KEY,
    transaction_date DATE NOT NULL,
    transaction_amount NUMERIC(10,2) NOT NULL CHECK (transaction_amount > 0),
    transaction_type VARCHAR(13) NOT NULL CHECK (transaction_type IN ('replenishment', 'withdrawal', 'transfer')),
    account_id INTEGER NOT NULL REFERENCES Account(account_id),
    recipient_account_id INTEGER REFERENCES Account(account_id),
	check_number INT GENERATED ALWAYS AS IDENTITY,
	deleted BOOLEAN NOT NULL DEFAULT FALSE
);