package com.thangBook.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.thangBook.bookservice", "com.thangBook.commonservice"})
public class BookserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookserviceApplication.class, args);
	}

}
