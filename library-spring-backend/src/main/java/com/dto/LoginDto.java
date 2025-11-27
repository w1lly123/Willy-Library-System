//資料傳輸物件Data Transfer Object 
package com.dto;

import lombok.Data;
//lombok自動getter/setter
@Data
public class LoginDto {
    private String phoneNumber;
    private String password;
}