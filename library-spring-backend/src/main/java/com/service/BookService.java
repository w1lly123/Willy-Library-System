package com.service;

import com.dao.BookDao;
import com.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookDao bookDao;
    
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return bookDao.findAllInventory();
    }

    @Transactional 
    public String borrowBook(Integer userId, Integer inventoryId) {
        return bookDao.borrowBook(userId, inventoryId);
    }

    @Transactional
    public String returnBook(Integer userId, Integer inventoryId) {
        String result = bookDao.returnBook(userId, inventoryId);
        
        // 判斷 SP 回傳的錯誤代碼
        if ("PERMISSION_DENIED".equals(result)) {
            return "這本書不是您借閱的，無法歸還";
        }
        // 如果是 SUCCESS 或其他 DB_ERROR，直接回傳
        return result; 
    }
}