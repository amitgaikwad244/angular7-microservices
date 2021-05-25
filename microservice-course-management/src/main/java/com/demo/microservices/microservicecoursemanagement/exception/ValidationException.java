package com.demo.microservices.microservicecoursemanagement.exception;

import org.springframework.validation.BindException;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private BindException bindException;

	public ValidationException(BindException bindException) {
		this.bindException = bindException;
	}

	public BindException getBindException() {
		return bindException;
	}

	public void setBindException(BindException bindException) {
		this.bindException = bindException;
	}

}
