CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS cards (
    number VARCHAR(255) PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    expiry_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL
);

TRUNCATE TABLE cards CASCADE;
TRUNCATE TABLE users CASCADE;

insert into users (username, password, role)
values ('danila', '123', 'ADMIN')
