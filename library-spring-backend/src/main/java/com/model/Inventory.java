package com.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inventory {
    private Integer inventoryId;
    private String isbn;
    private LocalDateTime storeTime;
    private Integer status;
    
    //在BookMapper使用JOIN時用到
    private String bookName;
    private String author;
}