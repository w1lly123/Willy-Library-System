package com.controller;

import com.dto.LoginDto;
import com.dto.RegistrationDto;
import com.dto.LoginResponseDto;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class UserController {
    //依賴注入
    @Autowired
    private UserService userService;
    //接收來自register的POST請求
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto dto) {
        try {
            //呼叫業務層進行註冊
            userService.register(dto);
            return ResponseEntity.ok(Collections.singletonMap("message", "註冊成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        LoginResponseDto response = userService.login(dto);
        
        if (response != null) {
            // 登入成功，回傳包含 userId 的完整資訊
            return ResponseEntity.ok(response);
        }
        
        // 登入失敗
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "帳號或密碼錯誤"));
    }
}