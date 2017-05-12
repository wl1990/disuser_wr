package com.test.resp;

public class CommResponse {
	private int errCode;
	private String errMessage;
	private Object data;


	public CommResponse() {
		errCode = 0;
		errMessage = "success";
	}
	public CommResponse(Object data) {
		this.errCode = 0;
		this.errMessage = "success";
		this.data = data;
	}

	public CommResponse(int errCode, String errMessage) {
		super();
		this.errCode = errCode;
		this.errMessage = errMessage;
	}
	public CommResponse(int errCode, String errMessage, Object data) {
		super();
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.data = data;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CommResponse{" +
				"errCode=" + errCode +
				", errMessage='" + errMessage + '\'' +
				", data=" + data +
				'}';
	}
}
