package com.demo.microservices.microservicecoursemanagement.model.response;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {

	private List<FieldError> fieldErrors = new ArrayList<>();

	private String actionError;

	public ValidationError() {
		super();
	}

	public void addFieldError(String path, String message) {
		FieldError error = new FieldError(path, message);
		fieldErrors.add(error);
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}	
	
	public String getActionError() {
		return actionError;
	}

	public void setActionError(String actionError) {
		this.actionError = actionError;
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder("");
		for (FieldError FieldError : fieldErrors) {
			strBuilder.append(FieldError.toString() + ", ");
		}
		if (strBuilder.length() > 2)
			strBuilder.deleteCharAt(strBuilder.length() - 2);
		return strBuilder.toString();
	}
}