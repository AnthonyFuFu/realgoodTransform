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
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.GroupUserDao;
import com.tkb.realgoodTransform.model.GroupUser;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupUserDaoImpl implements GroupUserDao {
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	
	@Override
	public void add(GroupUser groupUser) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(groupUser);
		String sql = "INSERT INTO GROUP_USER(GROUP_ID, USER_ID, CREATE_BY, CREATE_DATE)"
				+ "	VALUES (:group_id, :user_id, :create_by, now())";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public void update(GroupUser groupUser) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(groupUser);
		String sql = "UPDATE GROUP_USER SET GROUP_ID = :group_id, UPDATE_BY = :update_by,"
				+ " UPDATE_DATE = now()	WHERE USER_ID = :user_id";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public Integer checkUser(GroupUser groupUser) {
		String sql = "SELECT COUNT(*) FROM GROUP_USER WHERE USER_ID = ?";
		Integer count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, groupUser.getUser_id());
		return count;
	}

	@Override
	public Integer getGroupId(Integer id) {
		Integer group_id = null;
		String sql = "SELECT GROUP_ID FROM GROUP_USER WHERE USER_ID = ? ";
		try {
			group_id = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, id);
		} catch (RuntimeException e) {
			System.err.println(e);
		}
		return group_id;
	}

	@Override
	public List<GroupUser> getData(GroupUser groupUser) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = "SELECT * FROM GROUP_USER WHERE GROUP_ID = ? ";
		args.add(groupUser.getGroup_id());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<GroupUser>(GroupUser.class), args.toArray());
		
	}

}
