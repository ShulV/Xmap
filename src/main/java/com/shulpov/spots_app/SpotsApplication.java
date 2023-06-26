package com.shulpov.spots_app;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.*;
import java.util.Arrays;

@SpringBootApplication
public class SpotsApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpotsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpotsApplication.class, args);
		logger.atInfo().log("after run()");



		try {
			InetAddress address = InetAddress.getLocalHost();
			logger.atInfo().log("IP-address of the server: " + address.getHostAddress() + " "
					+ Arrays.toString(address.getAddress()) + " " +
					address.getCanonicalHostName());



		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		try (final DatagramSocket datagramSocket = new DatagramSocket()) {
			datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
			System.out.println(datagramSocket.getLocalAddress().getHostAddress());
		} catch (UnknownHostException | SocketException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		return modelMapper;
	}
}
