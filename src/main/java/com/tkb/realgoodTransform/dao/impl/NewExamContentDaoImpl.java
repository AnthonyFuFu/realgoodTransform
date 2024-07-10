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

import com.tkb.realgoodTransform.dao.NewExamContentDao;
import com.tkb.realgoodTransform.model.NewExamContent;



@Repository
public class NewExamContentDaoImpl implements NewExamContentDao {

	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgreJdbcTemplate;
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	
	@Autowired
	@Qualifier("fifthJdbcTemplate")
	private JdbcTemplate oracJdbcTemplate;
	
	
	@Override
	public List<NewExamContent> getList(NewExamContent newExamContent) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM NEWEXAM_CONTENT WHERE NEWEXAM_ID = ? ");
		args.add(newExamContent.getNewExam_id());
		sql.append("ORDER BY ID ");
		return postgreJdbcTemplate.query(sql.toString(),new BeanPropertyRowMapper<NewExamContent>(NewExamContent.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM NEWEXAM_CONTENT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(NewExamContent newExamContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamContent);
		String sql = "INSERT INTO NEWEXAM_CONTENT (ID, NEWEXAM_ID, ICON, TITLE, CONTENT, CREATE_BY, CREATE_DATE, "
				   + "UPDATE_BY, UPDATE_DATE )VALUES(:id, :newExam_id, :icon, :title, :content, :create_by, now(),"
				   + ":update_by, now() )";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(NewExamContent newExamContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamContent);
		String sql = "UPDATE NEWEXAM_CONTENT SET ICON = :icon, TITLE = :title, CONTENT = :content, "
				   + "UPDATE_BY = :update_by, UPDATE_DATE = now() WHERE ID = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM NEWEXAM_CONTENT WHERE ID = ? ";
		postgreJdbcTemplate.update(sql, new Object[] {id});

	}

	
//	================
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from newexam_content";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(NewExamContent newExamContent) {
		String sql = " INSERT into newexam_content (id,newExam_id,icon,title,content,create_by,create_date,update_by,update_date) VALUES (:id,:newExam_id,:icon,:title,:content,:create_by,:create_date,:update_by,:update_date)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET newExam_id = :newExam_id,icon = :icon,title = :title,content = :content,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(newExamContent);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);		
	}

}
