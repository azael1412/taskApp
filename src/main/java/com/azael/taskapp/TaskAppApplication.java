package com.azael.taskapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class TaskAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskAppApplication.class, args);
	}

}
