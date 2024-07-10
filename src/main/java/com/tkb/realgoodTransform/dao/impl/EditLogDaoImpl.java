package com.tkb.realgoodTransform.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.EditLogDao;
import com.tkb.realgoodTransform.model.EditLog;




@Repository
public class EditLogDaoImpl implements EditLogDao {

	@Autowired
	@Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	@Autowired
	@Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	
	@Override
	public void addLog(EditLog editLog) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(editLog);
		
		String sql = "INSERT INTO edit_log(id,function,data_id,action_type,content,create_by,create_date) "
				   + "VALUES(:id,:function,:data_id,:action_type,:content,:create_by,now())";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter ,keyHolder);
	}

	@Override
	public Integer getNextLogId() {
		Integer nextId= null;
		String sql = "SELECT ID FROM EDIT_LOG ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

}
