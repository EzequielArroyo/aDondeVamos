package com.github.ezequielarroyo.domain.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class UserServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
