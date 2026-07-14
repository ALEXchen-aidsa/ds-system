package com.ds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ds.mapper")
@EnableScheduling
public class DsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsSystemApplication.class, args);
    }
}