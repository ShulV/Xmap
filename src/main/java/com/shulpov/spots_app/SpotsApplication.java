package com.shulpov.spots_app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@EnableSwagger2
@SpringBootApplication
public class SpotsApplication {

	public static void main(String[] args) {
		System.out.println("MSG from main() before run()");
		SpringApplication.run(SpotsApplication.class, args);
		System.out.println("MSG from main() after run()");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("com.shulpov.spots_app.controllers"))
//				.paths(PathSelectors.any())
//				.build();
//	}
}
