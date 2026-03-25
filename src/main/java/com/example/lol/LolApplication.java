package com.example.lol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.lol"})
public class LolApplication {

	public static void main(String[] args) {
		SpringApplication.run(LolApplication.class, args);
	}

}
