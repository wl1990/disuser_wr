package com.test.handler;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.myenum.ErrorCode;
import com.test.resp.CommResponse;
import com.test.utils.NebulaException;





@ControllerAdvice
public class MyExceptionHandler {
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public CommResponse exceptionHandler0(Exception e, HttpServletResponse response) {
		return new CommResponse(500, e.getMessage());
	}
	
	@ExceptionHandler(NebulaException.class)
	@ResponseBody
	public CommResponse exceptionHandler(NebulaException e, HttpServletResponse response) {
		return new CommResponse(e.getErrCode(), e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public CommResponse validationExceptionHandler(MethodArgumentNotValidException e, HttpServletResponse response) {
		NebulaException nebulaException = new NebulaException.Builder().build(ErrorCode.VALIDATION_FAILED, e.getBindingResult());
		return exceptionHandler(nebulaException, response);
	}
}
