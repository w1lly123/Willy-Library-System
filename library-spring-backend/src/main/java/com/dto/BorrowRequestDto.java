package com.dto;

import lombok.Data;

@Data
public class BorrowRequestDto {
    private Integer userId;     
    private Integer inventoryId;
}