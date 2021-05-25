package com.demo.microservices.microservicecoursemanagement.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@PropertySource("classpath:message.properties")
@PropertySource("classpath:appconfig.properties")
@Service
public class PropertyUtils {

	@Autowired
	private Environment envTmp;

	private static Environment env;

	public static String getProperty(String key) {
		return env.getProperty(key);
	}

	@PostConstruct
	public void afterInit() {
		PropertyUtils.env = envTmp;
	}

}