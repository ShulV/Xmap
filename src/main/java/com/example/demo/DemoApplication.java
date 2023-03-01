package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("MSG from main() before run()");
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("MSG from main() after run()");
	}

}
