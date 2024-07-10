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

import com.tkb.realgoodTransform.dao.SchoolBulletinContentDao;
import com.tkb.realgoodTransform.model.SchoolBulletinContent;


@Repository
public class SchoolBulletinContentDaoImpl implements SchoolBulletinContentDao {
	
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
	public List<SchoolBulletinContent> getList(SchoolBulletinContent schoolBulletinContent) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN_CONTENT WHERE SCHOOL_BULLETIN_ID = ? ";
		args.add(schoolBulletinContent.getSchool_bulletin_id());
		sql += "ORDER BY ID";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletinContent>(SchoolBulletinContent.class),args.toArray());
	}

	@Override
	public List<SchoolBulletinContent> getList(int pageCount, int pageStart,
			SchoolBulletinContent schoolBulletinContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM SCHOOL_BULLETIN_CONTENT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		}catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(SchoolBulletinContent schoolBulletinContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(schoolBulletinContent);
		String sql = "INSERT INTO SCHOOL_BULLETIN_CONTENT(ID, SCHOOL_BULLETIN_ID, ICON, TITLE, CONTENT, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				   + "VALUES(:id, :school_bulletin_id, :icon, :title, :content, :create_by, now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void update(SchoolBulletinContent schoolBulletinContent) {
		List<Object> args = new ArrayList<Object>();
		String sql = "UPDATE SCHOOL_BULLETIN_CONTENT SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ";
		args.add(schoolBulletinContent.getUpdate_by());
		if(schoolBulletinContent.getIcon()!=null) {
			sql += "ICON = ?, ";
			args.add(schoolBulletinContent.getIcon());
		}
		if(schoolBulletinContent.getTitle()!=null) {
			sql += "TITLE = ?, ";
			args.add(schoolBulletinContent.getTitle());
		}
		if(schoolBulletinContent.getContent()!=null) {
			sql += "CONTENT = ? ";
			args.add(schoolBulletinContent.getContent());
		}
		sql += "WHERE ID = ?";
		args.add(schoolBulletinContent.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());

	}

	@Override
	public void delete(Integer id) {

		String sql = "DELETE FROM SCHOOL_BULLETIN_CONTENT WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}

	
//	================
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from school_bulletin_content";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(SchoolBulletinContent schoolBulletinContent) {
		String sql = " INSERT into school_bulletin_content (id,school_bulletin_id,icon,title,content,create_by,create_date,update_by,update_date) VALUES (:id,:school_bulletin_id,:icon,:title,:content,:create_by,:create_date,:update_by,:update_date)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET school_bulletin_id = :school_bulletin_id,icon = :icon,title = :title,content = :content,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(schoolBulletinContent);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
  