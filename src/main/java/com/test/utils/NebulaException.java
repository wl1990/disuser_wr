package com.test.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


public class NebulaException extends Exception {
	private static final long serialVersionUID = -5984598672608910431L;
	
	private int errCode;
	
	public NebulaException(int errCode, String msg) {
		super(msg);
        this.errCode = errCode;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public static class Builder {
		public NebulaException build(int errCode, BindingResult result) {
            String message = "Parameters invalidation failed.";
			if (result.hasErrors()) {
				List<FieldError> errors = result.getFieldErrors();
				List<String> messages = errors.stream().map(fieldErr -> {
					String fieldName = fieldErr.getField();
					String msg = fieldErr.getDefaultMessage();
					return fieldName + ": " + msg;
				}).collect(Collectors.toList());

				message = StringUtils.join(messages.toArray(), ";");
			}
			return new NebulaException(errCode, message);
		}
	}
}
