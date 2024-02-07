package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.GroupActionDao;
import com.tkb.realgoodTransform.model.GroupAction;

@Repository
public class GroupActionDaoImpl implements GroupActionDao {
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;

	@Override
	public void add(GroupAction groupAction) {
		
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(groupAction);
		
		String sql = " INSERT INTO GROUP_ACTION (GROUP_ID, ACITON_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				   + " VALUES(:group_id, :aciton_id, :create_by, now(), :update_by, now()) ";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();

		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
		
	}

	@Override
	public List<GroupAction> getData(GroupAction groupAction) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = "SELECT * FROM GROUP_ACTION WHERE GROUP_ID = ? ";
		args.add(groupAction.getGroup_id());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<GroupAction>(GroupAction.class), args.toArray());
		
	}

	@Override
	public void delete(GroupAction groupAction) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " DELETE FROM GROUP_ACTION WHERE GROUP_ID = ? ";
		args.add(groupAction.getGroup_id());
		
//		KeyHolder keyHolder = new GeneratedKeyHolder();
		
//		postgresqlJdbcTemplate.update(sql, args.toArray(), keyHolder);
		postgresqlJdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(GroupAction groupAction) {
		
		SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(groupAction);
		
		String sql = "INSERT INTO GROUP_ACTION (GROUP_ID, ACITON_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				   + " VALUES(:group_id, :aciton_id, :create_by, now(), :update_by, now()) ";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, parameterSource, keyHolder);
		
	}

}
