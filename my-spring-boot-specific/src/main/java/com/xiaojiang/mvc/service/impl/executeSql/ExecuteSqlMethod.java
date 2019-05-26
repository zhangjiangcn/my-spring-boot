package com.xiaojiang.mvc.service.impl.executeSql;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.beetl.sql.core.ConnectionSource;
import org.beetl.sql.core.ConnectionSourceHelper;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.MySqlStyle;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.wzy.sqltemplate.Configuration;
import org.wzy.sqltemplate.SqlMeta;
import org.wzy.sqltemplate.SqlTemplate;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaojiang.mvc.entity.SpecificInterfaceParam;
import com.xiaojiang.mvc.entity.SpecificInterfaceSql;
import com.xiaojiang.mvc.service.SpecificInterfaceParamService;
import com.xiaojiang.mvc.service.SpecificInterfaceSqlService;
import com.xiaojiang.util.JsonUtils;
import com.xiaojiang.util.ResultInfo;

/**
 * 此类已过期
 * 
 * @author ZhangJiang
 * @since 2019-05-26
 */
@Component
@Deprecated
public class ExecuteSqlMethod {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SpecificInterfaceSqlService specificInterfaceSqlService;
	@Autowired
	private SpecificInterfaceParamService specificInterfaceParamService;

	/**
	 * 执行sql语句
	 * 
	 * @param map
	 * @return
	 */
	public Object execSql(Map<String, Object> map, String dmlMark) {

		if (!(StringUtils.equals(dmlMark, "query") || StringUtils.equals(dmlMark, "update"))) {
			return ResultInfo.error("参数dmlMark无法识别");
		}

		String dataType = (String) map.get("dataType");
		if (StringUtils.isBlank(dataType)) {
			return ResultInfo.error("请求参数 dataType 为null或空字符串");
		}

		// 获取sql语句
		List<SpecificInterfaceSql> dList = this.findSqlList(dataType);

		if (dList.size() == 0) {
			return ResultInfo.error("根据请求参数 dataType 未查找对应接口数据");
		}

		HashSet<Object> hashSet = new HashSet<>();
		for (SpecificInterfaceSql sql : dList) {
			String dataSpace = sql.getDataSpace();
			if (StringUtils.isBlank(dataSpace)) {
				return ResultInfo.error("数据空间 data_space 为null或空字符串");
			}
			if (!hashSet.add(dataSpace)) {
				return ResultInfo.error("数据空间 data_space 重复命名");
			}
		}

		ExecutorService threadPool = Executors.newCachedThreadPool();
		Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
		
		for (int i = 0; i < dList.size(); i++) {
			SpecificInterfaceSql sql = dList.get(i);
			
			// 执行线程
			threadPool.execute(new Runnable() {

				@Override
				@SuppressWarnings("unchecked")
				public void run() {
					
					String dataSql = sql.getDataSql();
					String dataSpace = sql.getDataSpace();
					if (StringUtils.isBlank(dataSql)) {
						resultMap.put(dataSpace, "sql语句 data_sql 为null或空字符串");
						return;
					}
					
					Object result = null;

					// 参数Map集合中添加数据，返回新的集合。
					Object paramObject = new Object();
					try {
						paramObject = paramMapAddData(map, sql);
					} catch (Exception e) {
						resultMap.put(dataSpace, e.getMessage());
						return;
					}
					if (paramObject instanceof ResultInfo) {
						ResultInfo<Object> info = (ResultInfo<Object>) paramObject;
						resultMap.put(dataSpace, info.getMessage());
						return;
					}
					Map<String, Object> paramMap = (Map<String, Object>) paramObject;

					// 选择sql模板引擎
					String templateEngine = sql.getSqlTemplateEngine();
					if (StringUtils.isBlank(templateEngine) || StringUtils.equals("mybatis", templateEngine)) {
						try {
							// 使用sqlTemplate查询数据库
							result = processSqlBySqlTemplate(paramMap, dataSql, dmlMark);
						} catch (Exception e) {
							resultMap.put(dataSpace, e.getMessage());
							return;
						}
					} else if (StringUtils.equals("beetl", templateEngine)) {
						try {
							// 使用beelSql查询数据库
							result = processSqlByBeetl(paramMap, dataSql, dmlMark);
						} catch (Exception e) {
							resultMap.put(dataSpace, e.getMessage());
							return;
						}
					} else {
						resultMap.put(dataSpace, "选择sql模板引擎错误 ===> sql_template_engine");
						return;
					}
					if (StringUtils.equals(dmlMark, "query")) {
						// 选择结果数据格式
						String resultDataFormat = sql.getResultDataFormat();
						if (StringUtils.isBlank(resultDataFormat) || StringUtils.equals("array", resultDataFormat)) {
							// list 对象
							resultMap.put(dataSpace, result);
						} else if (StringUtils.equals("object", resultDataFormat)) {
							// map 对象
							resultMap.put(dataSpace, listToMap(result));
						} else {
							resultMap.put(dataSpace, "选择结果数据格式错误 ===> result_data_format");
						}
					} else {
						resultMap.put(dataSpace, result);
					}
				}
			});
		}
		threadPool.shutdown();
		try {
			while (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
			}
		} catch (InterruptedException e) {
			resultMap.clear();
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 根据多个param_key获取数据列表（多个param_key逗号分隔）
	 */
	public List<SpecificInterfaceParam> findListByKey(String keys) {
		QueryWrapper<SpecificInterfaceParam> queryWrapper = new QueryWrapper<SpecificInterfaceParam>();
		List<String> list = Arrays.asList(keys.split(","));
		queryWrapper.in("id", list);
		return specificInterfaceParamService.list(queryWrapper);
	}

	/**
	 * 参数Map集合中添加数据，返回新的集合。
	 * 
	 * @param map
	 * @param resultMap
	 * @param sql
	 * @param dataSpace
	 * @return
	 */
	public Object paramMapAddData(Map<String, Object> map, SpecificInterfaceSql sql) throws Exception {
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>(map);

		// 获取数据参数
		String dataParamId = sql.getDataParamId();
		if (StringUtils.isNotBlank(dataParamId)) {
			List<SpecificInterfaceParam> keyList = this.findListByKey(dataParamId);
			for (SpecificInterfaceParam param : keyList) {
				String paramKey = param.getParamKey();
				if (paramMap.get(paramKey) != null) {
					return ResultInfo.error("请求参数和根据 data_param_id 匹配的数据 中存在重复参数名");
				}
				if (StringUtils.isBlank(paramKey)) {
					return ResultInfo.error("根据 data_param_id 匹配的数据中 param_key 存在null或空字符串");
				}
				paramMap.put(paramKey, param.getParamValue());
			}
		}
		return paramMap;
	}

	/**
	 * 字符串逗号分隔，每一项家单引号，再返回字符串
	 * 
	 * @param str
	 * @return
	 */
	public String strAppendQuotation(String str) {
		StringBuffer sb = new StringBuffer();
		String[] temp = str.split(",");
		for (int i = 0; i < temp.length; i++) {
			if (!"".equals(temp[i]) && temp[i] != null)
				sb.append("'" + temp[i] + "',");
		}
		String result = sb.toString();
		String tp = result.substring(result.length() - 1, result.length());
		if (",".equals(tp))
			return result.substring(0, result.length() - 1);
		else
			return result;
	}

	/**
	 * 根据dataType查找sql列表
	 * 
	 * @param dataType
	 * @return
	 */
	public List<SpecificInterfaceSql> findSqlList(String dataType) {
		SpecificInterfaceSql specificInterfaceSql = new SpecificInterfaceSql();
		specificInterfaceSql.setDataType(dataType);
		QueryWrapper<SpecificInterfaceSql> queryWrapper = new QueryWrapper<SpecificInterfaceSql>(specificInterfaceSql);
		return specificInterfaceSqlService.list(queryWrapper);
	}

	/**
	 * 处理请求参数
	 * 
	 * @param set
	 * @return
	 */
	public Map<String, Object> parameterHandle(Set<Entry<String, String[]>> set) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Entry<String, String[]> entry : set) {
			String key = entry.getKey();
			List<String> list = Arrays.asList(entry.getValue());
			if (list.size() == 1) {
				map.put(key, list.get(0));
			} else if (list.size() > 1) {
				map.put(key, list);
			}
		}
		return map;
	}

	/**
	 * 使用sqlTemplate查询数据库
	 * 
	 * @param map
	 * @param sql
	 * @return
	 */
	public Object processSqlBySqlTemplate(Map<String, Object> map, String sql, String dmlMark) throws Exception {
		Configuration configuration = new Configuration();
		SqlTemplate template = configuration.getTemplate(sql);
		SqlMeta process = template.process(map);
		List<Object> parameter = process.getParameter();
		if (StringUtils.equals(dmlMark, "query")) {
			return jdbcTemplate.queryForList(process.getSql(), parameter.toArray());
		} else {
			return jdbcTemplate.update(process.getSql(), parameter.toArray());
		}
	}

	/**
	 * beetl直接执行SQL模板
	 * 
	 * @param map
	 * @param sql
	 * @return
	 */
	public Object processSqlByBeetl(Map<String, Object> map, String sql, String dmlMark) throws Exception {

		ConnectionSource source = ConnectionSourceHelper.getSingle(dataSource);
		SQLManager sqlManager = new SQLManager(new MySqlStyle(), source);

//		String key = "auto._gen_" + sql;
//		SQLSource sqlSource = new SQLSource(key, sql);
//		sqlManager.getSqlLoader().addSQL(key, sqlSource);
//		SQLResult sqlResult = sqlManager.getSQLResult(sqlSource, map);

		// beetl直接执行SQL模板
		if (StringUtils.equals(dmlMark, "query")) {
			return sqlManager.execute(sql, Map.class, map);
		} else {
			return sqlManager.executeUpdate((String) map.get("sql"), map);
		}
	}
	
	/**
	 * List转Map
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> listToMap(Object list) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 循环数据,进行数据的拼装
		for (Map<String, Object> data : (List<Map<String, Object>>) list) {
			if (data == null) {
				continue;
			}
			for (Entry<String, Object> entry : data.entrySet()) {
				String dataKey = entry.getKey();
				Object dataValue = entry.getValue();
				List<Object> dataList = (List<Object>) map.get(dataKey);
				if (dataList == null) {
					dataList = new ArrayList<Object>();
				}
				dataList.add(dataValue);
				map.put(dataKey, dataList);
			}
		}
		return map;
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
	public Map<String, Object> getParamByForm(Set<Entry<String, String[]>> set) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String indexMark = "@index";
		for (Entry<String, String[]> entry : set) {
			String key = entry.getKey();
			List<String> list = Arrays.asList(entry.getValue());
			String value = list.get(0);
			Matcher matcher = keyRegExp(key);
			if (matcher.find() && list.size() == 1) {
				this.formIndexParamHandle(map, indexMark, value, matcher);
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
	 * form表单带有下标类似数组元素参数处理
	 * @param map
	 * @param indexMark
	 * @param value
	 * @param matcher
	 */
	@SuppressWarnings("unchecked")
	private void formIndexParamHandle(Map<String, Object> map, String indexMark, String value, Matcher matcher) {
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
	
	@Test
	public void test() {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", "jack");
		System.out.println(map.get("name"));
	}

}
