package com.shulpov.spots_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpotsApplication {

	public static void main(String[] args) {
		System.out.println("MSG from main() before run()");
		SpringApplication.run(SpotsApplication.class, args);
		System.out.println("MSG from main() after run()");
	}

}
