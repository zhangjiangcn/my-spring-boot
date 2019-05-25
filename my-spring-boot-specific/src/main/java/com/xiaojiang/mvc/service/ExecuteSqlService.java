package com.xiaojiang.mvc.service;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;

public interface ExecuteSqlService {

	Object executeSql(HttpServletRequest request, String dmlMark);
	
}
