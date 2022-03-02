package com.jzj.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * @Author Jzj
 * @Date 2021/11/25 22:39
 * @Version 1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class GateMain {
    public static void main(String[] args) {
        SpringApplication.run(GateMain.class,args);
    }
}
