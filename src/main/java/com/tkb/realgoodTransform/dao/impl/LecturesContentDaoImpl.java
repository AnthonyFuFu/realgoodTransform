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

import com.tkb.realgoodTransform.dao.LecturesContentDao;
import com.tkb.realgoodTransform.model.LecturesContent;



@Repository
public class LecturesContentDaoImpl implements LecturesContentDao {
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	
	@Autowired
	@Qualifier("fifthJdbcTemplate")
	private JdbcTemplate oracJdbcTemplate;
	
	
	
	@Override
	public List<LecturesContent> getList(LecturesContent lecturesContent) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM LECTURES_CONTENT WHERE LECTURES_ID = ? ");
		args.add(lecturesContent.getLectures_id());
		sql.append("ORDER BY ID");
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<LecturesContent>(LecturesContent.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM LECTURES_CONTENT ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			e.printStackTrace();
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(LecturesContent lecturesContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lecturesContent);
		String sql = "INSERT INTO LECTURES_CONTENT (ID, LECTURES_ID, ICON, TITLE, CONTENT, CREATE_BY, "
				   + "CREATE_DATE, UPDATE_BY, UPDATE_DATE) VALUES (:id, :lectures_id, :icon, :title, :content, :create_by, "
				   + "now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(LecturesContent lecturesContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lecturesContent);
		String sql = "UPDATE LECTURES_CONTENT SET ICON = :icon, TITLE = :title, CONTENT = :content, "
				   + "UPDATE_BY = :update_by, UPDATE_DATE = now() WHERE ID = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM LECTURES_CONTENT WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}
	
	
//	=================

	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from lectures_content";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(LecturesContent lecturesContent) {
		String sql = " INSERT into lectures_content (id,lectures_id,icon,title,content,create_by,create_date,update_by,update_date) VALUES (:id,:lectures_id,:icon,:title,:content,:create_by,:create_date,:update_by,:update_date)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET lectures_id = :lectures_id,icon = :icon,title = :title,content = :content,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date  = :update_date "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lecturesContent);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
