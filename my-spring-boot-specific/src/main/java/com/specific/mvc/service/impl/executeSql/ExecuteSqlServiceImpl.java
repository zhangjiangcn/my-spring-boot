package com.specific.mvc.service.impl.executeSql;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.specific.mvc.service.ExecuteSqlService;
import com.specific.util.JsonUtils;
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
			map = this.getParamByForm(set);
		} else if (StringUtils.equals(contentType, "application/json")) {
			try {
				BufferedReader br = request.getReader();
				map = this.getParamMapByBR(br);
			} catch (IOException e) {
				return ResultInfo.error(e.getMessage());
			}
		} else {
			map = executeSqlMethod.parameterHandle(set);
		}
		return executeSqlMethod.execSql(map, dmlMark);
	}

	/**
	 * 获取参数Map通过请求数据BufferedReader对象
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> getParamMapByBR(BufferedReader br) throws IOException {
		String str = "";
		StringBuffer wholeStr = new StringBuffer();
		while ((str = br.readLine()) != null) {
			wholeStr.append(str);
		}
		Map<String, Object> map = new LinkedHashMap<>();
		str = wholeStr.toString();
		if (StringUtils.isNotBlank(str)) {
			JsonUtils<Object> jsonUtils = new JsonUtils<Object>();
			map = jsonUtils.getMapFromStr(str);
		}
		return map;
	}

	/**
	 * 获取Map集合通过form表单数据
	 * 
	 * @param set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParamByForm(Set<Entry<String, String[]>> set) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String indexMark = "@index";
		for (Entry<String, String[]> entry : set) {
			String key = entry.getKey();
			List<String> list = Arrays.asList(entry.getValue());
			String value = list.get(0);
			Matcher matcher = keyRegExp(key);
			if (matcher.find() && list.size() == 1) {
				String k = matcher.group(1);
				Integer index = Integer.valueOf(matcher.group(2));
				String name = matcher.group(3);
				Object object = map.get(k);
				if (object == null) {
					// 不存在于map集合
					Map<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put(name, value);
					hashMap.put(indexMark, index);
					List<Object> objList = new ArrayList<Object>();
					objList.add(hashMap);
					map.put(k, objList);
				} else {
					// 存在于map集合
					List<Object> objList = (List<Object>) object;
					boolean bool = false;
					for (Object obj : objList) {
						Map<String, Object> hashMap = (Map<String, Object>) obj;
						Integer indexVal = (Integer) hashMap.get(indexMark);
						if (index == indexVal) {
							hashMap.put(name, value);
							bool = true;
							break;
						}
					}
					if (!bool) {
						Map<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put(name, value);
						hashMap.put(indexMark, index);
						objList.add(hashMap);
					}
				}
			} else {
				if (list.size() == 1) {
					map.put(key, value);
				} else if (list.size() > 1) {
					map.put(key, list);
				}
			}
		}
		return map;
	}

	/**
	 * key 正则匹配
	 * 
	 * @param str
	 * @return
	 */
	public Matcher keyRegExp(String str) {
		String regex = "^([a-zA-Z].*)\\[(\\d+)\\]\\.([a-zA-Z].*)$";
		Pattern r = Pattern.compile(regex);
		return r.matcher(str);
	}

}
