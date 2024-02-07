package com.tkb.realgoodTransform.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.UserLoginLogDao;
import com.tkb.realgoodTransform.model.UserLoginLog;

@Repository
public class UserLoginLogDaoImpl implements UserLoginLogDao {
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;

	@Override
	public void addUserLoginLog(UserLoginLog userLoginLog) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(userLoginLog);
		String sql = "INSERT INTO USER_LOGIN_LOG( "
				+ "	ACCOUNT, IP, STATUS, CREATE_DATE) "
				+ "	VALUES (:account, :ip, :status, :create_date)";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

}
