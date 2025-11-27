package com.service;

import com.dto.LoginDto;
import com.dto.RegistrationDto;
import com.dao.UserDao;
import com.model.User;
import com.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(RegistrationDto dto) {
        User user = new User();
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserName(dto.getUserName());
        
        String result = userDao.save(user);
        
        if ("DUPLICATE".equals(result)) {
            throw new RuntimeException("號碼已被註冊");
        }
        return "註冊成功";
    }
    public LoginResponseDto login(LoginDto dto) {
        User user = userDao.findByPhoneNumber(dto.getPhoneNumber());
        
        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            
            userDao.updateLastLoginTime(user.getUserId());
            
            // 回傳包含 ID 的物件
            return new LoginResponseDto("登入成功", user.getUserId(), user.getUserName());
        }
        return null;
    }
}