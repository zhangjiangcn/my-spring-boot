package com.specific.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
  * 使用jackson包
 *   1.对象转json字符串 
 *   2.字符串转化为对象 
 *   3.字符串转化为ArrayList对象 
 *   4.字符串转化为ArrayList的HashMap对象
 *   5.HashMap对象转对象
 * @param <T>
 * @author ZhangJiang
 * @since 2019-02-05
 */
public class JsonUtils<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
	private static ObjectMapper objectMapper;
	@SuppressWarnings("unused")
	private static final String TIME_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private String timeFormat;

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
		objectMapper.setDateFormat(new SimpleDateFormat(timeFormat));
	}

	public JsonUtils() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
	}

	public JsonUtils(String timeFormat) {
		objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat(timeFormat));
	}

	/**
	 * 对象转json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSon(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			LOGGER.error("对象转json字符串", e);
		}
		return "";
	}

	/**
	 * 字符串转化为对象
	 * 
	 * @param v
	 * @param json
	 * @return
	 */
	public T getObjectFromStr(Class<T> v, String json) {
		try {
			return objectMapper.readValue(json.getBytes(), objectMapper.constructType(v));
		} catch (IOException e) {
			LOGGER.error("字符串转化为对象异常", e);
		}
		return null;
	}

	/**
	 * 字符串转化为Map集合
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMapFromStr(String json) {
		try {
			return objectMapper.readValue(json, Map.class);
		} catch (IOException e) {
			LOGGER.error("字符串转化为Map集合异常", e);
		}
		return null;
	}

	/**
	 * HashMap对象转对象
	 * 
	 * @param v
	 * @param map
	 * @return
	 */
	public T getObjectFromMap(Class<T> v, HashMap<String, Object> map) {
		return objectMapper.convertValue(map, objectMapper.getTypeFactory().constructType(v));
	}

	/**
	 * 字符串转化为ArrayList对象
	 * 
	 * @param v
	 * @param json
	 * @return
	 */
	public List<T> getArrayListObjectFromStr(Class<T> v, String json) {
		try {
			return objectMapper.readValue(json.getBytes(),
					objectMapper.getTypeFactory().constructParametricType(ArrayList.class, v));
		} catch (IOException e) {
			LOGGER.error("字符串转化为ArrayList对象异常", e);
		}
		return null;
	}

	/**
	 * 字符串转化为ArrayList的HashMap对象
	 * 
	 * @param json
	 * @return
	 */
	public List<T> getArrayListMapFromStr(String json) {
		try {
			return objectMapper.readValue(json.getBytes(),
					objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HashMap.class));
		} catch (IOException e) {
			LOGGER.error("字符串转化为ArrayList的HashMap对象异常", e);
		}
		return null;
	}

	public static void main(String[] args) {

//        JsonUtils<CarMessage> jsonUtils = new JsonUtils<CarMessage>();
//        CarMessage objectFromStr = jsonUtils.getObjectFromStr(CarMessage.class, "{\"type\":1,\"driverId\":1,\"driverName\":\"18868877621\",\"driverLocation\":{\"longitude\":121.581206,\"latitude\":29.864368,\"driverType\":10,\"status\":1}}");
//        objectFromStr.getDriverName();
//        String s = jsonUtils.toJSon(objectFromStr);
//        System.out.println(s);
//        CarMessage objectFromStr2 = jsonUtils.getObjectFromStr(CarMessage.class, s);
//        String s1 = jsonUtils.toJSon(objectFromStr);
//        Long driverId = objectFromStr2.getDriverId();
//        System.out.println(s1);
//
//        List<CarMessage> list = new ArrayList<CarMessage>();
//        list.add(objectFromStr);
//        list.add(objectFromStr2);
//        String s3 = jsonUtils.toJSon(list);
//        System.out.println(s3);
//        List<CarMessage> listObjectFromStr = jsonUtils.getArrayListObjectFromStr(CarMessage.class, s3);
//        System.out.println(jsonUtils.toJSon(listObjectFromStr));
	}
}