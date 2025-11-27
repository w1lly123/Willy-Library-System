package com.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String message;
    private Integer userId; //登入成功後回傳ID給前端
    private String userName;
}