package com.huntercodexs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.huntercodexs.*")
public class SpringBatchSimpleJobDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchSimpleJobDemoApplication.class, args);
	}

}
