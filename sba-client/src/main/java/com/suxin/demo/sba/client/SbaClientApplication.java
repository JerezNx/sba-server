package com.suxin.demo.sba.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class SbaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbaClientApplication.class, args);
    }

}
