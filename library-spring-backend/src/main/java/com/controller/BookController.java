package com.controller;

import com.model.Inventory;
import com.service.BookService;
import com.dto.BorrowRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

//處理RESTFUL API的控制器，將回傳的Java物件轉為JSON格式
@RestController
//只要網址是http://localhost:8081/api/books開頭的請求都會轉接進來
@RequestMapping("/api/books")
public class BookController {
    //依賴注入
    @Autowired
    private BookService bookService;
    //前端發送GET請求時執行
    @GetMapping("/inventory")
    public List<Inventory> getInventory() {
        return bookService.getAllInventory();
    }

 @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowRequestDto dto) {

        
        String result = bookService.borrowBook(dto.getUserId(), dto.getInventoryId());
        
        // HTTP 狀態碼回傳
        if ("SUCCESS".equals(result)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "借閱成功"));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", result));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody BorrowRequestDto dto) {
        String result = bookService.returnBook(dto.getUserId(), dto.getInventoryId());
        
        if ("SUCCESS".equals(result)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "還書成功"));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", result));
        }
    }
}