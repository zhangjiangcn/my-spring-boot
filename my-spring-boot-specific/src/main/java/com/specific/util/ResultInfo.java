package com.specific.util;

public class ResultInfo<T> {

	public static final Integer OK = 1;
	public static final Integer ERROR = -1;

	private Integer code;
	private String message;
	private T data;

	public ResultInfo() {
		this.code = ResultInfo.OK;
	}

	public ResultInfo(T data) {
		this.code = ResultInfo.OK;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public static ResultInfo<Object> error(String message) {
		ResultInfo<Object> resultInfo = new ResultInfo<Object>();
		resultInfo.setCode(ResultInfo.ERROR);
		resultInfo.setMessage(message);
		return resultInfo;
	}
	
	public static ResultInfo<Object> error(String message,Object data) {
		ResultInfo<Object> error = ResultInfo.error(message);
		error.setData(data);
		return error;
	}

}
