package com.xiaojiang.config;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.github.abel533.sql.SqlMapper;

@Configuration
public class MyBatisPlusConfiguration {
	
	@Autowired
	SqlSessionTemplate sqlSession;
	
	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}
	
	@Bean
	public SqlMapper sqlMapper() {
		return new SqlMapper(sqlSession);
	}
	
}