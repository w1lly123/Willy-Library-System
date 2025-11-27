DROP TABLE IF EXISTS borrowing_record;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS book;

-- 1. Book (書籍基本資料)
CREATE TABLE book (
    isbn VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    introduction TEXT
);

-- 2. User (使用者)
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP
);

-- 3. Inventory (庫存實體)
-- Status: 1=在庫, 2=出借中, 3=整理中, 4=遺失, 5=損毀, 6=廢棄
CREATE TABLE inventory (
    inventory_id SERIAL PRIMARY KEY,
    isbn VARCHAR(20) REFERENCES book(isbn),
    store_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status INTEGER NOT NULL DEFAULT 1
);

-- 4. Borrowing Record (借閱紀錄)
CREATE TABLE borrowing_record (
    record_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    inventory_id INTEGER REFERENCES inventory(inventory_id),
    borrowing_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_time TIMESTAMP
);

CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_inventory_status ON inventory(status);