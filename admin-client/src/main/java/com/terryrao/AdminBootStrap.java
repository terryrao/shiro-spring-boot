package com.terryrao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
//@MapperScan("com.terry") 会造成两次扫描 com.terry 然后报bean 重复的错误
public class AdminBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(AdminBootStrap.class, args);
    }



}
