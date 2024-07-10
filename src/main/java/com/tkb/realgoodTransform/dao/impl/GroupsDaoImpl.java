package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.tkb.realgoodTransform.dao.GroupsDao;
import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.User;



@Repository
public class GroupsDaoImpl implements GroupsDao {
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;

	@Override
	public Integer getCount(Groups groups) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) FROM GROUPS ";	//之後要加判斷，除資訊部外，其他群組不秀最高權限的群組
		
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public List<Groups> getList(int pageCount, int pageStart, Groups groups) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM GROUPS ";	//之後要加判斷，除資訊部外，其他群組不秀最高權限的群組

		/*
		//舊架構的判斷
		if(!"admin".equals(groups.getAccount())) {
			sql += " WHERE ID <> '1' ";
		}
		*/
		if(!(null == groups.getName()) || "".equals(groups.getName())) {
			sql += " WHERE NAME LIKE ? ";
			args.add("%" + groups.getName() + "%");
		}
		
		sql += " ORDER BY ID LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Groups>(Groups.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		
		Integer nextId = null;
		String sql = " SELECT ID FROM GROUPS ORDER BY ID DESC LIMIT 1 ";
		
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
				
		return nextId;
	}

	@Override
	public void add(Groups groups) {
		
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(groups);
		
		String sql = " INSERT INTO GROUPS (ID, NAME, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				   + " VALUES(:id, :name, :create_by, now(), :update_by, now()) ";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
		
	}

	@Override
	public Groups getGroup(Groups groups) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = "SELECT * FROM GROUPS WHERE ID = ? ";
		
		args.add(groups.getId());
		
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Groups>(Groups.class), args.toArray());
		
	}

	@Override
	public void delete(Groups groups) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " DELETE FROM GROUPS WHERE ID = ? ";
		
		args.add(groups.getId());
		
		postgresqlJdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public List<Groups> getGroupsList() {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM GROUPS";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Groups>(Groups.class), args.toArray());
	}

	@Override
	public void update(Groups groups) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE GROUPS SET NAME = ? , UPDATE_BY = ? , UPDATE_DATE = NOW() WHERE ID = ? ";
		args.add(groups.getName());
		args.add(groups.getUpdate_by());
		args.add(groups.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public Integer getGroupId(String group_name) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = "Select id from groups where name = ?";
		
		args.add(group_name);
		
		return postgresqlJdbcTemplate.queryForObject(sql,Integer.class,args.toArray());
	}

	@Override
	public List<Map<String, Object>> judegeRepeat(String group_name) {
		List<Object> args = new ArrayList<>();
		String sql = "select * from groups where name = ?";
		args.add(group_name);
		
		return postgresqlJdbcTemplate.queryForList(sql,args.toArray());
	}
	
	@Override
	public Integer getGroupId(User user) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT GROUP_ID FROM GROUP_USER WHERE USER_ID=?";
		args.add(user.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

}
