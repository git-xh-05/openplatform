package com.openplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.continew.starter.extension.crud.annotation.EnableCrudApi;

@EnableCrudApi
@SpringBootApplication
@MapperScan(basePackages = {"com.openplatform.admin.**.mapper", "com.openplatform.open.**.mapper"})
public class OpenPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenPlatformApplication.class, args);
    }
}