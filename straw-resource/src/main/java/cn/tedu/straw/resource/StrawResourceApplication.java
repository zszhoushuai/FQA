package cn.tedu.straw.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StrawResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawResourceApplication.class, args);
    }

}
