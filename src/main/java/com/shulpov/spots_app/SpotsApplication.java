package com.shulpov.spots_app;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpotsApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpotsApplication.class);
	public static void main(String[] args) {
		logger.atInfo().log("MSG from main() before run()");
		SpringApplication.run(SpotsApplication.class, args);
		logger.atInfo().log("MSG from main() after run()");
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		return modelMapper;
	}
}
