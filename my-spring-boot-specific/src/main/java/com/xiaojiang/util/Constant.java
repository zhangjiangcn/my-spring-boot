package com.xiaojiang.util;

public enum Constant {

	OK(1), ERROR(-1);
	private Integer code;

	Constant(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
