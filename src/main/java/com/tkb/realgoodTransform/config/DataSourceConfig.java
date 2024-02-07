package com.tkb.realgoodTransform.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
public class DataSourceConfig {

	@Bean(name = "firstDataSource")
    @Primary
	@ConfigurationProperties(prefix = "spring.datasource.druid.first")
	public DataSource primaryDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "secondDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.second")
	public DataSource primaryDataSource2() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "thirdDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.third")
	public DataSource primaryDataSource3() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "fourthDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.fourth")
	public DataSource primaryDataSource4() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name = "fifthDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid.fifth")
	public DataSource primaryDataSource5() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name = "postgresqlJdbcTemplate")
	public JdbcTemplate postgresqlJdbcTemplate(@Qualifier("firstDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "postgresqlJdbcTemplate2")
	public NamedParameterJdbcTemplate postgresqlJdbcNameTemplate(@Qualifier("firstDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean(name = "secondJdbcTemplate")
	public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "thirdJdbcTemplate")
	public JdbcTemplate thirdJdbcTemplate(@Qualifier("thirdDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "fourthJdbcTemplate")
	public JdbcTemplate fourthJdbcTemplate(@Qualifier("fourthDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean(name = "fifthJdbcTemplate")
	public JdbcTemplate fifthJdbcTemplate(@Qualifier("fifthDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
}
