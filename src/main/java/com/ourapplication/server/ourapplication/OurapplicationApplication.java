package com.ourapplication.server.ourapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OurapplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OurapplicationApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowCredentials(true).allowedOriginPatterns("*").allowedMethods("*");
			}
		};
	}
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
