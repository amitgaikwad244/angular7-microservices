package com.demo.microservices.microservicecoursemanagement.common;

import java.text.MessageFormat;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.demo.microservices.microservicecoursemanagement.exception.FileTypeNotSupportedException;
import com.demo.microservices.microservicecoursemanagement.exception.ValidationException;
import com.demo.microservices.microservicecoursemanagement.model.response.ResponseObj;
import com.demo.microservices.microservicecoursemanagement.utils.PropertyUtils;
import com.demo.microservices.microservicecoursemanagement.validation.ValidatorService;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private ResponseObj res;

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseObj requestBodyError(HttpMessageNotReadableException e) {
		if (e.getMostSpecificCause() instanceof InvalidFormatException) {
			InvalidFormatException ex = (InvalidFormatException) e.getMostSpecificCause();
			res.addFieldError(ex.getPath().get(0).getFieldName(), PropertyUtils.getProperty("exception.invalidField"));
		} else {
			res.setActionError(PropertyUtils.getProperty("exception.invalidRequestBody"));
			logger.error("Exception: ", e);
		}
		return res;
	}

	@ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class, ValidationException.class })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseObj processValidationError(Exception ex) {

		BindingResult result = null;
		if (ex instanceof BindException) {
			result = ((BindException) ex).getBindingResult();
		}
		if (ex instanceof MethodArgumentNotValidException) {
			result = ((MethodArgumentNotValidException) ex).getBindingResult();
		}
		if (ex instanceof ValidationException) {
			result = ((ValidationException) ex).getBindException().getBindingResult();
		}

		res.setValidationError(validatorService.processErrors(result));
		return res;
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseObj maxUploadFileSizeExceededError(FileUploadException e) {

		if (e instanceof FileSizeLimitExceededException) {
			FileSizeLimitExceededException ex = (FileSizeLimitExceededException) e;
			res.addFieldError(ex.getFieldName(), MessageFormat.format(PropertyUtils.getProperty("exception.fileSizeExceeded"), ex.getPermittedSize() / 1024 / 1024));
		}
		if (e instanceof SizeLimitExceededException) {
			SizeLimitExceededException ex = (SizeLimitExceededException) e;
			res.setActionError(MessageFormat.format(PropertyUtils.getProperty("exception.requestSizeExceeded"), ex.getPermittedSize() / 1024 / 1024));
		}
		return res;
	}

	@ExceptionHandler(FileTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseObj fileTypeNotSupported(FileTypeNotSupportedException e) {
		res.addFieldError(e.getFieldName(), PropertyUtils.getProperty("exception.fileTypeNotSupported"));
		return res;
	}

	@ExceptionHandler(NotOfficeXmlFileException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseObj notOfficeXmlFileException(NotOfficeXmlFileException e) {
		res.setActionError(PropertyUtils.getProperty("exception.notValidXlsx"));
		return res;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseObj error(Exception e) {
		logger.error("Exception: ", e);
		res.setActionError(MessageFormat.format(PropertyUtils.getProperty("exception.globalError"), res.getData().get("referenceId")));
		return res;
	}
}