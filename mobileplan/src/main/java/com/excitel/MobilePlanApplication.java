package com.excitel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication

@CrossOrigin(origins = "*")
@EnableFeignClients(basePackages = "com.excitel.external")
@PropertySource("classpath:application.properties")
@EnableRetry
@EnableAspectJAutoProxy
public class MobilePlanApplication {
	public static void main(String[] args) {
		SpringApplication.run(MobilePlanApplication.class, args);
	}
}
