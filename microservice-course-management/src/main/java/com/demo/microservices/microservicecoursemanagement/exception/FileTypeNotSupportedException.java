package com.demo.microservices.microservicecoursemanagement.exception;

public class FileTypeNotSupportedException extends RuntimeException {

	private static final long serialVersionUID = -8511301757392753865L;

	private String fieldName;

	public FileTypeNotSupportedException() {
		super();
	}

	public FileTypeNotSupportedException(String message) {
		super(message);
	}

	public FileTypeNotSupportedException(String message, Throwable t) {
		super(message, t);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
