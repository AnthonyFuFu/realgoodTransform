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
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.CourseDiscountContentDao;
import com.tkb.realgoodTransform.model.CourseDiscount;
import com.tkb.realgoodTransform.model.CourseDiscountContent;

@Repository
public class CourseDiscountContentDaoImpl implements CourseDiscountContentDao{
	
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
	public List<CourseDiscountContent> getList(CourseDiscountContent courseDiscountContent) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM COURSE_DISCOUNT_CONTENT WHERE COURSE_DISCOUNT_ID = ? ";
		args.add(courseDiscountContent.getCourse_discount_id());
		
		sql += " ORDER BY ID ";
		
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<CourseDiscountContent>(CourseDiscountContent.class),args.toArray());
		
	}
	
	@Override
	public Integer getNextId() {

		Integer nextId = null;
		String sql = " SELECT ID FROM COURSE_DISCOUNT_CONTENT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
		
	}
	
	@Override
	public void add(CourseDiscountContent courseDiscountContent) {
		
		String sql = " INSERT INTO COURSE_DISCOUNT_CONTENT(ID, COURSE_DISCOUNT_ID, ICON, TITLE, CONTENT, IMAGE, CREATE_BY, "
				   + " CREATE_DATE, UPDATE_BY, UPDATE_DATE)VALUES(?, ?, ?, ?, ?, ?, ?, now(), ?, now()) ";
		
		postgreJdbcTemplate.update(sql, new Object[] { courseDiscountContent.getId(), courseDiscountContent.getCourse_discount_id(), 
				courseDiscountContent.getIcon(), courseDiscountContent.getTitle(), courseDiscountContent.getContent(), courseDiscountContent.getImage(),
				courseDiscountContent.getCreate_by(), courseDiscountContent.getUpdate_by() });
		
	}

	@Override
	public void update(CourseDiscountContent courseDiscountContent) {
		
		String sql = " UPDATE COURSE_DISCOUNT_CONTENT SET ICON = ?, TITLE = ?, CONTENT = ?, IMAGE = ?, "
				   + " UPDATE_BY = ?, UPDATE_DATE = SYSDATE WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { courseDiscountContent.getIcon(), courseDiscountContent.getTitle(),
				courseDiscountContent.getContent(), courseDiscountContent.getImage(), courseDiscountContent.getUpdate_by(), courseDiscountContent.getId() });
		
	}

	@Override
	public void delete(Integer id) {
		
		String sql = " DELETE FROM COURSE_DISCOUNT_CONTENT WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { id });
		
	}			

	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from COURSE_DISCOUNT_CONTENT";
		return oracJdbcTemplate.queryForList(sql);
	}

	@Override
	public void updateNormalData(CourseDiscountContent courseDiscountContent) {
		String sql = " INSERT into COURSE_DISCOUNT_CONTENT "
				+ " (id,course_discount_id,icon,title,content,create_by,create_date,update_by,update_date,image) VALUES "
				+ " (:id,:course_discount_id,:icon,:title,:content,:create_by,:create_date,:update_by,CAST(:update_date AS TIMESTAMP),:image) "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET course_discount_id = :course_discount_id,icon = :icon,title = :title,content = :content,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = CAST(:update_date AS TIMESTAMP),image = :image "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(courseDiscountContent);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}
}
