//APP入口
package com.esunbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com")
public class LibrarySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibrarySystemApplication.class, args);
    }
}