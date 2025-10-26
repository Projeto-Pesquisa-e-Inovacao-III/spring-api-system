package com.spring.ApiSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.spring.ApiSystem.repository")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ApiSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSystemApplication.class, args);
	}

}
