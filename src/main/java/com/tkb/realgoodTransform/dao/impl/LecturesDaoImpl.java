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

import com.tkb.realgoodTransform.dao.LecturesDao;
import com.tkb.realgoodTransform.model.Lectures;



@Repository
public class LecturesDaoImpl implements LecturesDao {

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
	public List<Lectures> getList(int pageCount, int pageStart, Lectures lectures) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT L.*, C.NAME AS CATEGORY_NAME "
				         + "FROM LECTURES L "
				         + "LEFT JOIN LECTURES_CATEGORY C ON C.ID = CAST(L.CATEGORY AS INTEGER) WHERE 1=1 ");
		if(lectures.getTitle() != null && !"".equals(lectures.getTitle())) {
			sql.append("AND L.TITLE LIKE ? ");
			args.add("%" + lectures.getTitle() + "%");
		}
		if(lectures.getCategory() != null && !"".equals(lectures.getCategory())) {
			sql.append("AND L.CATEGORY = ? ");
			args.add(lectures.getCategory());
		}
		sql.append("ORDER BY L.LECTURES_TOP DESC, L.BEGIN_DATE DESC , L.ID DESC LIMIT ? OFFSET ? " );
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<Lectures>(Lectures.class),args.toArray());
	}

	@Override
	public List<Lectures> getFrontList(Lectures lectures) {
		String sql = "SELECT L.*,LC.NAME AS CATEGORY_NAME "
				   + "FROM LECTURES L "
				   + "LEFT JOIN LECTURES_CATEGORY LC "
				   + "ON CAST(L.CATEGORY AS INTEGER) = LC.ID "
				   + "WHERE TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
			   sql += " ORDER BY L.LECTURES_TOP DESC, L.BEGIN_DATE DESC , L.ID DESC LIMIT 5";
	     
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Lectures>(Lectures.class));
	}

	@Override
	public List<Lectures> getFrontList(int pageCount, int pageStart, Lectures lectures, String search_sort) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT L.*, C.NAME AS CATEGORY_NAME FROM LECTURES L "
				   + "LEFT JOIN LECTURES_CATEGORY C ON C.ID = CAST(L.CATEGORY AS INTEGER) WHERE 1 = 1 "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		if(lectures.getCategory() != null && !"".equals(lectures.getCategory())) {
			sql += " AND L.CATEGORY = ? ";
			args.add(lectures.getCategory());
		}
		if(lectures.getFare() != null && !"".equals(lectures.getFare())) {
			sql += " AND L.FARE = ? ";
			args.add(lectures.getFare());
		}
		if("week".equals(lectures.getDate())) {
			sql +=  "AND( "
			     + "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
			     + "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
			     + "OR "
			     + "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
			     + "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
			     + "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
			     + ") ";
			args.add(lectures.getWeek_begin_date());
			args.add(lectures.getWeek_begin_date());
			args.add(lectures.getWeek_begin_date());
			args.add(lectures.getWeek_end_date());
			args.add(lectures.getWeek_begin_date());
		} else if("month".equals(lectures.getDate())) {
			sql += "AND "
			     + "( "
			     + "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
			     + "OR "
			     + "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
			     + ") ";
					
		}
		if("".equals(search_sort) || search_sort == null || "new".equals(search_sort)) {
			sql += "ORDER BY L.LECTURES_TOP DESC, L.BEGIN_DATE DESC , L.ID DESC ";
		} else {
			sql += " ORDER BY L.CLICK_RATE DESC , L.ID DESC ";
		}
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Lectures>(Lectures.class),args.toArray());
	}


	@Override
	public Integer getCount(Lectures lectures) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM LECTURES WHERE 1=1 ");
		if(lectures.getTitle() != null && !"".equals(lectures.getTitle())) {
			sql.append("AND TITLE LIKE ? ");
			args.add("%" + lectures.getTitle() + "%");
		}
		if(lectures.getCategory() != null && !"".equals(lectures.getCategory())) {
			sql.append("AND CATEGORY = ? ");
			args.add(lectures.getCategory());
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public Integer getFrontCount(Lectures lectures) {
		List<Object>args = new ArrayList<>();
		
		String sql = "SELECT COUNT(*) FROM LECTURES L WHERE 1=1 "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		
		if(lectures.getCategory() != null && !"".equals(lectures.getCategory())) {
			sql += " AND L.CATEGORY = ? ";
			args.add(lectures.getCategory());
		}
		if(lectures.getFare() != null && !"".equals(lectures.getFare())) {
			sql += " AND L.FARE = ? ";
			args.add(lectures.getFare());
		}
		if("week".equals(lectures.getDate())) {
			sql += "AND( "
				     + "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				     + "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				     + "OR "
				     + "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				     + "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				     + "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				     + ") ";
			args.add(lectures.getWeek_begin_date());
			args.add(lectures.getWeek_begin_date());
			args.add(lectures.getWeek_begin_date());
			args.add(lectures.getWeek_end_date());
			args.add(lectures.getWeek_begin_date());
		} else if("month".equals(lectures.getDate())) {
			sql += "AND "
				+ "( "
				+ "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ "OR "
				+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ ") ";
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}


	@Override
	public Lectures getData(Lectures lectures) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES WHERE ID = ? ";
		args.add(lectures.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Lectures>(Lectures.class),args.toArray());
	}

	@Override
	public Lectures getFrontData(Lectures lectures) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES WHERE ID = ? ";
		args.add(lectures.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Lectures>(Lectures.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM LECTURES ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			e.printStackTrace();
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(Lectures lectures) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lectures);
		String sql = "INSERT INTO LECTURES (ID, TITLE, CATEGORY, IMAGE, BEGIN_DATE, END_DATE, CLICK_RATE, "
				   + "CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, CONTENT, FARE, PLACE, PHONE, FARE_MONEY, "
				   + "LECTURES_TOP, SEO_CONTENT, LECTURE_TYPE_ID, ENCRYPTURL, PRODUCT_CATEGORY)VALUES(:id, :title, :category, :image, :begin_date, :end_date , 0, "
				   + ":create_by, now(), :update_by, now(), :content, :fare, :place, :phone, :fare_money, "
				   + ":lectures_top, :seo_content, :lecture_type_id, :encrypturl, :product_category) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(Lectures lectures) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lectures);
		String sql = "UPDATE LECTURES SET TITLE = :title, CATEGORY = :category, IMAGE = :image, "
				   + "BEGIN_DATE = :begin_date, END_DATE = :end_date, UPDATE_BY = :update_by, UPDATE_DATE = now(), "
				   + "CONTENT = :content, FARE = :fare, PLACE = :place, PHONE = :phone, FARE_MONEY = :fare_money, "
				   + "LECTURES_TOP = :lectures_top, SEO_CONTENT = :seo_content, LECTURE_TYPE_ID = :lecture_type_id, PRODUCT_CATEGORY = :product_category WHERE ID = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);

	}

	@Override
	public void updateClickRate(Lectures lectures) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE LECTURES SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(lectures.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM LECTURES WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql,new Object[] {id});
	}

	@Override
	public Integer checkTopCount(Lectures lectures) {
		String sql = "SELECT COUNT(*) FROM LECTURES WHERE CAST(LECTURES_TOP AS INTEGER) = 1 ";
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public List<Lectures> getFreeCourse() {
		String sql = "SELECT * FROM LECTURES WHERE CAST(FARE AS INTEGER) = 1 "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Lectures>(Lectures.class));
	}

	@Override
	public List<Map<String, Object>> getPlaceName(Integer id) {
		String sql = "SELECT DISTINCT PLACENAME FROM LECTURES_PLACE WHERE LECTURES_ID = ? ";
		return postgresqlJdbcTemplate.queryForList(sql,new Object[] {id});
	}

	
	
//	===================
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from lectures";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(Lectures lectures) {
		String sql = " INSERT into lectures (id, title, area, category, image, begin_date, end_date, click_rate, create_by, create_date, update_by, update_date, content, fare, place, phone, fare_money, lectures_top, seo_content, lecture_type_id, encrypturl, product_category) VALUES (:id, :title, :area, :category, :image, :begin_date, :end_date, :click_rate, :create_by, :create_date, :update_by, :update_date, :content, :fare, :place, :phone, :fare_money, :lectures_top, :seo_content, :lecture_type_id, :encrypturl, :product_category)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET title = :title, area = :area, category = :category, image = :image, begin_date = :begin_date, end_date = :end_date, click_rate = :click_rate, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, content = :content, fare = :fare, place = :place, phone = :phone, fare_money = :fare_money, lectures_top = :lectures_top, seo_content = :seo_content, lecture_type_id = :lecture_type_id, encrypturl = :encrypturl, product_category = :product_category\r\n"
				+ " "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lectures);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
