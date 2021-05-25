package com.demo.microservices.microservicecoursemanagement.common.logs;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.microservices.microservicecoursemanagement.exception.ValidationException;
import com.demo.microservices.microservicecoursemanagement.model.SessionAttributes;
import com.demo.microservices.microservicecoursemanagement.model.response.ResponseObj;

@Aspect
@Component
public class ExceptionLoggingAspect {

	@Autowired
	private ResponseObj res;

	@Autowired
	private CustomLogger logger;

	@Autowired
	private SessionAttributes sessionAttrs;

	@Autowired
	private HttpServletRequest request;

	@AfterThrowing(pointcut = "execution(* com.demo.microservices.microservicecoursemanagement.controller..*.*(..))", throwing = "e")
	public void logException(JoinPoint joinPoint, Exception e) {
		// Ignoring mdrtemplate.exception.ValidationException since it will be handled
		// by GlobalExceptionHandler
		if (!(e instanceof ValidationException)) {
			res.addData("referenceId", logger.logException(joinPoint, e, sessionAttrs, request));
		}
	}

}
