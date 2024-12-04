package com.g96.ftms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.g96.ftms.repository")
@EnableJpaAuditing
@EnableScheduling

public class Sep490G96FtmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(Sep490G96FtmsApplication.class, args);
	}
}
