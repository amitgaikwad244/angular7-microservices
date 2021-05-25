package com.demo.microservices.microservicecoursemanagement.validation;

import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import com.demo.microservices.microservicecoursemanagement.exception.ValidationException;
import com.demo.microservices.microservicecoursemanagement.model.response.ValidationError;

@Service
public class ValidatorService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SmartValidator validator;

	public void validate(Object obj, BindingResult vResult, Class<?> c) {
		validator.validate(obj, vResult, c);
		if (vResult.hasErrors())
			throw new ValidationException(new BindException(vResult));
	}

	public ValidationError processErrors(BindingResult result) {
		ValidationError dto = new ValidationError();

		for (FieldError fieldError : result.getFieldErrors()) {
			String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
			dto.addFieldError(fieldError.getField(), localizedErrorMessage);
		}

		for (ObjectError globalError : result.getGlobalErrors()) {
			String localizedErrorMessage = resolveLocalizedErrorMessage(globalError);
			dto.setActionError(localizedErrorMessage);
		}

		return dto;
	}

	public void cleanRejectedObject(Object data, BindingResult result) {
		List<FieldError> fieldErrors = result.getFieldErrors();
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(data);
		for (FieldError fieldError : fieldErrors) {
			if ("Size".equals(fieldError.getCode())) {
				wrapper.setPropertyValue(fieldError.getField(), StringUtils.abbreviate(fieldError.getRejectedValue().toString(), (Integer) fieldError.getArguments()[1]));
			}
		}
	}

	public static void addFieldError(final ConstraintValidatorContext context, String fieldName, String template) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(template).addPropertyNode(fieldName).addConstraintViolation();
	}

	public static void addClassError(final ConstraintValidatorContext context, String template, String[]... params) {
		HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
		hibernateContext.disableDefaultConstraintViolation();

		for (String[] param : params) {
			if (param.length != 2)
				throw new IllegalArgumentException("addClassError() params array should always be of size 2");
			hibernateContext.addMessageParameter(param[0], param[1]);
		}
		hibernateContext.buildConstraintViolationWithTemplate((template == null || template.isEmpty()) ? hibernateContext.getDefaultConstraintMessageTemplate() : template).addBeanNode().addConstraintViolation();
	}

	public static void changeValidationMessage(final ConstraintValidatorContext context, String template, String[]... params) {
		HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
		hibernateContext.disableDefaultConstraintViolation();
		for (String[] param : params) {
			if (param.length != 2)
				throw new IllegalArgumentException("changeValidationMessage() params array should always be of size 2");
			hibernateContext.addMessageParameter(param[0], param[1]);
		}
		hibernateContext.buildConstraintViolationWithTemplate((template == null || template.isEmpty()) ? hibernateContext.getDefaultConstraintMessageTemplate() : template).addConstraintViolation();
	}

	private String resolveLocalizedErrorMessage(ObjectError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(fieldError, currentLocale);
	}

}
