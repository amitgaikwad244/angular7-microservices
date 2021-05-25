package com.demo.microservices.microservicecoursemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MicroserviceCourseManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCourseManagementApplication.class, args);
	}
	
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}
}
