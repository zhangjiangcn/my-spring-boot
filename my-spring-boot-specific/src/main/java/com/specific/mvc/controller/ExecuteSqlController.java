package com.specific.mvc.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.specific.mvc.service.ExecuteSqlService;

/**
 * 调用接口执行sql，返回结果
 * 
 * @author ZhangJiang
 * @since 2019-02-03
 */
@RestController
@RequestMapping(value = "/executeSql", method = { RequestMethod.GET, RequestMethod.POST })
public class ExecuteSqlController {

	@Autowired
	ExecuteSqlService executeSqlService;

	/**
	 * 根据请求参数更新数据，执行相应sql，返回数据   
	 * 	dmlMark: query | update
	 * @param request
	 * @param dmlMark
	 * @return
	 */
	@RequestMapping(value = "/{dmlMark}")
	public Object executeSql(HttpServletRequest request,@PathVariable String dmlMark){
		// 根据已知条件查询数据库,返回结果集
		return executeSqlService.executeSql(request,dmlMark);
	}
}
