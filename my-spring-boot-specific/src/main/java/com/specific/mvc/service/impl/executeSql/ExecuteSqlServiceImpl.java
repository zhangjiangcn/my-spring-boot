package com.specific.mvc.service.impl.executeSql;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.specific.mvc.service.ExecuteSqlService;
import com.specific.util.ResultInfo;

@Service
public class ExecuteSqlServiceImpl implements ExecuteSqlService {

	@Autowired
	ExecuteSqlMethod executeSqlMethod;

	/**
	 * 请求数据处理
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public Object executeSql(HttpServletRequest request, String dmlMark) {
		// 获取请求参数键值对集合
		Set<Entry<String, String[]>> set = request.getParameterMap().entrySet();
		// 获取请求数据类型
		String contentType = request.getContentType();

		Map<String, Object> map = null;
		// 根据已知条件查询数据库,返回结果集
		if (StringUtils.startsWith(contentType, "multipart/form-data")) {
			map = executeSqlMethod.getParamByForm(set);
		} else if (StringUtils.equals(contentType, "application/json")) {
			try {
				BufferedReader br = request.getReader();
				map = executeSqlMethod.getParamMapByBR(br);
			} catch (IOException e) {
				return ResultInfo.error(e.getMessage());
			}
		} else {
			map = executeSqlMethod.parameterHandle(set);
		}
		return executeSqlMethod.execSql(map, dmlMark);
	}

}
