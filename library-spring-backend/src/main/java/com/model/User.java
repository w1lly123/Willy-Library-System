package com.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String phoneNumber;
    private String password;
    private String userName;
    private LocalDateTime registrationTime;
    private LocalDateTime lastLoginTime;
}