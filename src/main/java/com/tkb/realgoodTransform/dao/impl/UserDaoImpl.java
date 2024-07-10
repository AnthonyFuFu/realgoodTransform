package com.tkb.realgoodTransform.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.UserDao;
import com.tkb.realgoodTransform.model.User;



/**
 * 使用者Dao實作類
 * @author Ken
 * @version 創建時間：2016-02-24
 */
@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	
	@Override
	public User login(User user) {
	    String sql = "SELECT * FROM USER_ACCOUNT WHERE ACCOUNT = UPPER(?) AND STATUS = 1";
	    List<User> list = postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<User>(User.class), new Object[]{user.getAccount()});
	    return list.size() == 0 ? null : list.get(0);
	}
	
	@Override
	public Integer checkAccount(User user) {
		String sql = " SELECT count(*) FROM USER_ACCOUNT WHERE ALIVE = 1 AND ACCOUNT = UPPER(?)";
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,new Object[]{ user.getAccount() });
	}
	@Override
	public Integer checkStatus(User user) {
		String sql = " SELECT count(*) FROM USER_ACCOUNT WHERE ALIVE = 1  AND STATUS = 1 AND ACCOUNT = UPPER(?) ";
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,new Object[]{ user.getAccount() });
		
	}
	
	@Override
	public Integer getUserId(User user) {
		String sql = " SELECT ID FROM USER_ACCOUNT WHERE ACCOUNT = ?";
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,new Object[]{ user.getAccount() });
	}
	

	
}
