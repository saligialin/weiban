package com.wbteam.weiban;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wbteam.weiban.mapper")
public class WeibanApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeibanApplication.class, args);
    }

}
