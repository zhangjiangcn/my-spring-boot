package com.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

public class Test {
	
	@org.junit.Test
	public void index() throws IOException {
		Properties properties = Resources.getResourceAsProperties("jdbc.properties");
        //定义数据源的xml片段
        String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
        		"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n" + 
        		"<mapper namespace=\"com.xiaojiang.mvc.mapper.SpecificInterfaceSqlMapper\">\r\n" + 
        		"	\r\n" + 
        		"	<select id=\"test\" parameterType=\"java.util.Map\" resultType=\"java.util.LinkedHashMap\">\r\n" + 
        		"		SELECT * FROM specific_interface_param\r\n" + 
        		"		<where>\r\n" + 
        		"			<if test=\"key != null and key != ''\">\r\n" + 
        		"				param_key = #{key}\r\n" + 
        		"			</if>\r\n" + 
        		"		</where>\r\n" + 
        		"	</select>\r\n" + 
        		"</mapper>\r\n" + 
        		"";
        //初始化XPathParser
        XPathParser xPathParser = new XPathParser(xml,false,properties);
        //解析表达式，获取XNode对象
        XNode xnode = xPathParser.evalNode("//select");
        //下面调用对应的函数
        System.out.println(xnode);
        System.out.println(xnode.getValueBasedIdentifier());
        System.out.println(xnode.getStringAttribute("id"));
        System.out.println(xnode.getStringAttribute("resultType"));
	}
}
