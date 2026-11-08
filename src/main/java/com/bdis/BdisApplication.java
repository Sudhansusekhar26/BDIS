package com.bdis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BdisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdisApplication.class, args);
    }
}
