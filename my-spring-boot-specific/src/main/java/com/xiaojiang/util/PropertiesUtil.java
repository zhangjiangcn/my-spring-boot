package com.xiaojiang.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {
	public static Resource resource;
	public static Properties props;
	
	/**
	 * 加载配置文件
	 * 
	 * @param path
	 */
	public static void loadProperties(String path) {
		try {
			props = PropertiesLoaderUtils.loadProperties(new ClassPathResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定key的value
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
}
