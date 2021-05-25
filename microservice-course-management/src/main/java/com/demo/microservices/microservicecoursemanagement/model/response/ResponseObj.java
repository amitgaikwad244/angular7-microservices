package com.demo.microservices.microservicecoursemanagement.model.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.demo.microservices.microservicecoursemanagement.utils.PropertyUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.ScopedProxyMode;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@JsonAutoDetect(fieldVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
@JsonInclude(value = Include.NON_NULL)
public class ResponseObj {

	private Map<String, Object> data;

	private ValidationError validationError;

	private ResponseMsg msg;

	public ResponseObj() {
		super();
	}

	public void addData(String key, Object value) {
		if (data == null)
			data = new HashMap<>();
		data.put(key, value);
	}

	public void setMsg(String message, ResponseMsgType type) {
		if (message.startsWith("${") && message.endsWith("}"))
			message = PropertyUtils.getProperty(message.substring(2, message.length() - 1));

		msg = new ResponseMsg(message, type);
	}

	public void addFieldError(String fieldPath, String message) {
		if (validationError == null)
			validationError = new ValidationError();
		validationError.addFieldError(fieldPath, message);
	}

	public void setActionError(String error) {
		if (validationError == null)
			validationError = new ValidationError();
		validationError.setActionError(error);
	}

	public void setValidationError(ValidationError validationError) {
		this.validationError = validationError;
	}

	// GETTERS
	@JsonProperty
	public Map<String, Object> getData() {
		return data;
	}

	@JsonProperty
	public ValidationError getValidationError() {
		return validationError;
	}

	@JsonProperty
	public ResponseMsg getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return String.format("{msg=%s,validationError=%s,data=%s}",msg,validationError,data);
	}
	
}
