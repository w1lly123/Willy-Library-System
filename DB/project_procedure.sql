-- =============================================
-- 1. 使用者註冊 (Register)
-- =============================================
DROP ROUTINE IF EXISTS sp_register_user(VARCHAR, VARCHAR, VARCHAR, VARCHAR);

CREATE OR REPLACE PROCEDURE sp_register_user(
    p_phone_number VARCHAR,
    p_password VARCHAR,
    p_user_name VARCHAR,
    INOUT p_result VARCHAR
) LANGUAGE plpgsql AS $$
BEGIN
    -- 檢查手機號碼是否重複
    IF EXISTS (SELECT 1 FROM users WHERE phone_number = p_phone_number) THEN
        p_result := 'DUPLICATE';
    ELSE
        INSERT INTO users (phone_number, password, user_name)
        VALUES (p_phone_number, p_password, p_user_name);
        p_result := 'SUCCESS';
    END IF;
END;
$$;

-- =============================================
-- 2. 使用者登入查詢 (Login Query)
-- =============================================
-- 清除舊版本 (包含 Function 與 Procedure 以避免衝突)
DROP ROUTINE IF EXISTS sp_get_user_by_phone(VARCHAR);
DROP ROUTINE IF EXISTS sp_get_user_by_phone(VARCHAR, INTEGER, VARCHAR, VARCHAR, TIMESTAMP);

CREATE OR REPLACE PROCEDURE sp_get_user_by_phone(
    IN p_phone_number VARCHAR,
    OUT o_user_id INTEGER,
    OUT o_password VARCHAR,
    OUT o_user_name VARCHAR,
    OUT o_last_login_time TIMESTAMP
) LANGUAGE plpgsql AS $$
BEGIN
    -- 使用 OUT 參數回傳單筆資料，取代 RETURN TABLE
    SELECT user_id, password, user_name, last_login_time
    INTO o_user_id, o_password, o_user_name, o_last_login_time
    FROM users
    WHERE phone_number = p_phone_number;
END;
$$;

-- =============================================
-- 3. 更新最後登入時間 (Update Login Time)
-- =============================================
DROP ROUTINE IF EXISTS sp_update_login_time(INTEGER);

CREATE OR REPLACE PROCEDURE sp_update_login_time(p_user_id INTEGER)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE users SET last_login_time = CURRENT_TIMESTAMP WHERE user_id = p_user_id;
END;
$$;

-- =============================================
-- 4. 查詢所有庫存 (Get All Inventory)
-- =============================================
-- 使用 REFCURSOR (游標) 回傳多筆資料，讓 Java 不需要寫 SELECT
DROP ROUTINE IF EXISTS sp_get_all_inventory();
DROP ROUTINE IF EXISTS sp_get_all_inventory(REFCURSOR);

CREATE OR REPLACE PROCEDURE sp_get_all_inventory(
    INOUT p_refcursor REFCURSOR = 'rs_inventory'
) LANGUAGE plpgsql AS $$
BEGIN
    OPEN p_refcursor FOR
    SELECT i.inventory_id, i.isbn, i.status, b.name, b.author
    FROM inventory i
    JOIN book b ON i.isbn = b.isbn
    ORDER BY i.inventory_id;
END;
$$;

-- =============================================
-- 5. 借閱書籍 (Borrow Book)
-- =============================================
DROP ROUTINE IF EXISTS sp_borrow_book(INTEGER, INTEGER, VARCHAR);

CREATE OR REPLACE PROCEDURE sp_borrow_book(
    p_user_id INTEGER,
    p_inventory_id INTEGER,
    INOUT p_result VARCHAR
) LANGUAGE plpgsql AS $$
DECLARE
    v_current_status INTEGER;
BEGIN
    -- [交易鎖定] 使用 FOR UPDATE 鎖定該筆庫存，防止並發超賣
    SELECT status INTO v_current_status FROM inventory WHERE inventory_id = p_inventory_id FOR UPDATE;

    IF v_current_status IS NULL THEN
        p_result := 'BOOK_NOT_FOUND';
        RETURN;
    ELSIF v_current_status != 1 THEN -- 1=在庫
        p_result := 'BOOK_NOT_AVAILABLE';
        RETURN;
    END IF;

    -- 更新狀態 (2=出借中)
    UPDATE inventory SET status = 2 WHERE inventory_id = p_inventory_id;
    
    -- 新增借閱紀錄
    INSERT INTO borrowing_record (user_id, inventory_id, borrowing_time) 
    VALUES (p_user_id, p_inventory_id, CURRENT_TIMESTAMP);

    p_result := 'SUCCESS';
EXCEPTION WHEN OTHERS THEN
    -- 捕捉其他未預期的資料庫錯誤
    p_result := 'DB_ERROR';
END;
$$;

-- =============================================
-- 6. 歸還書籍 (Return Book)
-- =============================================
DROP ROUTINE IF EXISTS sp_return_book(INTEGER, INTEGER, VARCHAR);

CREATE OR REPLACE PROCEDURE sp_return_book(
    p_user_id INTEGER,
    p_inventory_id INTEGER,
    INOUT p_result VARCHAR
) LANGUAGE plpgsql AS $$
DECLARE
    v_rows_affected INTEGER;
BEGIN
    -- 嘗試更新借閱紀錄 (填寫歸還時間)
    -- 條件: 必須是該使用者借的 + 尚未歸還 (return_time IS NULL)
    UPDATE borrowing_record SET return_time = CURRENT_TIMESTAMP
    WHERE inventory_id = p_inventory_id AND user_id = p_user_id AND return_time IS NULL;
    
    -- 檢查是否成功更新 (權限檢查)
    GET DIAGNOSTICS v_rows_affected = ROW_COUNT;

    IF v_rows_affected = 0 THEN
        p_result := 'PERMISSION_DENIED'; -- 不是該使用者借的，或已歸還
        RETURN;
    END IF;

    -- 更新庫存狀態 (1=在庫)
    UPDATE inventory SET status = 1 WHERE inventory_id = p_inventory_id;
    
    p_result := 'SUCCESS';
END;
$$;