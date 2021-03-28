package com.demo.microservices.netflixzuulapigatewayserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

	
	@GetMapping("/currencyExchangeServiceFallBack")
    public String userServiceFallBackMethod() {
        return "Currency-Exchange Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/currencyConversionServiceFallBack")
    public String departmentServiceFallBackMethod() {
        return "Currency-Conversion Service is taking longer than Expected." +
                " Please try again later";
    }
}
