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

import com.tkb.realgoodTransform.dao.NewExamDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.NewExam;



@Repository
public class NewExamDaoImpl implements NewExamDao{

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
	public List<NewExam> getList(int pageCount, int pageStart, NewExam newExam) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT NE.*,C.NAME AS CATEGORY_NAME "
				         + "FROM NEWEXAM NE "
				         + "LEFT JOIN NEWEXAM_CATEGORY C ON C.ID = CAST(NE.CATEGORY AS INTEGER) ");
		if(newExam.getTitle() != null && !"".equals(newExam.getTitle())) {
			sql.append("WHERE TITLE LIKE ? ");
			args.add("%" + newExam.getTitle() + "%");
		}
		sql.append("ORDER BY NE.BEGIN_DATE DESC, NE.ID DESC LIMIT ? OFFSET ? ");
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<NewExam>(NewExam.class),args.toArray());
	}

	@Override
	public List<NewExam> getFrontList(NewExam newExam) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM NEWEXAM WHERE CATEGORY = ? "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "
				   + "AND ID != ? ORDER BY UPDATE_DATE LIMIT 3 ";
				   args.add(newExam.getCategory());
				   args.add(newExam.getId());
				   
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NewExam>(NewExam.class),args.toArray());
	}

	@Override
	public List<NewExam> getFrontList(int pageCount, int pageStart, NewExam newExam, String search_sort) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT NE.*, C.NAME AS CATEGORY_NAME FROM NEWEXAM NE "
				   + "LEFT JOIN NEWEXAM_CATEGORY C  ON C.ID = CAST(NE.CATEGORY AS INTEGER) WHERE 1=1 "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		if(newExam.getCategory() != null && !"".equals(newExam.getCategory())) {
			sql += "AND NE.CATEGORY = ? ";
			args.add(newExam.getCategory());
		}
		if("week".equals(newExam.getDate())) {
			sql +=  "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				 +	"AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE "
				 +	"AND( "
			     + "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
			     + "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
			     + "OR "
			     + "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
			     + "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
			     + "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
			     + ") ";
			args.add(newExam.getWeek_begin_date());
			args.add(newExam.getWeek_begin_date());
			args.add(newExam.getWeek_begin_date());
			args.add(newExam.getWeek_end_date());
			args.add(newExam.getWeek_begin_date());
		} else if("month".equals(newExam.getDate())) {
			sql += "AND "
			     + "( "
			     + "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
			     + "OR "
			     + "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
			     + ") ";
					
		}
		
		if("".equals(search_sort) || search_sort == null || "new".equals(search_sort)) {
			sql += " ORDER BY NE.BEGIN_DATE DESC, NE.ID DESC ";
		} else {
			sql += " ORDER BY NE.CLICK_RATE DESC, NE.ID DESC ";
		}
		
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NewExam>(NewExam.class),args.toArray());
	}

	@Override
	public List<NewExam> getFrontList(int pageCount, int pageStart, NewExam newExam, List<Area> areaList,
			String search_sort) {
		return null;
	}

	@Override
	public List<NewExam> getFrontList() {
		String sql = " SELECT N.*, NC.NAME AS CATEGORY_NAME "
				   + " FROM NEWEXAM N "
				   + " LEFT JOIN NEWEXAM_CATEGORY NC ON cast(N.CATEGORY as integer)=NC.ID"
				   + " WHERE 1 = 1 "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "				   
				   + " ORDER BY N.UPDATE_DATE DESC LIMIT 5 ";
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NewExam>(NewExam.class));
		}

	@Override
	public Integer getCount(NewExam newExam) {
		List<Object>args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM NEWEXAM ");
		if(newExam.getTitle() != null && !"".equals(newExam.getTitle())) {
			sql.append("WHERE TITLE LIKE ? ");
			args.add("%" + newExam.getTitle() + "%");
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public Integer getFrontCount(NewExam newExam) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM NEWEXAM NE WHERE 1=1 "
					+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
					+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		if(newExam.getCategory() != null && !"".equals(newExam.getCategory())) {
			sql += "AND NE.CATEGORY = ? ";
			args.add(newExam.getCategory());
		}
		if("week".equals(newExam.getDate())) {
			sql +=  "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
					 +	"AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE "
					 +	"AND( "
				     + "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				     + "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				     + "OR "
				     + "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				     + "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				     + "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				     + ") ";
			args.add(newExam.getWeek_begin_date());
			args.add(newExam.getWeek_begin_date());
			args.add(newExam.getWeek_begin_date());
			args.add(newExam.getWeek_end_date());
			args.add(newExam.getWeek_begin_date());
		} else if("month".equals(newExam.getDate())) {
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
	public Integer getFrontCount(NewExam newExam, List<Area> areaList) {
		return null;
	}

	@Override
	public NewExam getData(NewExam newExam) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM NEWEXAM WHERE ID = ? ";
		args.add(newExam.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<NewExam>(NewExam.class),args.toArray());
	}

	@Override
	public NewExam getFrontData(NewExam newExam) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM NEWEXAM WHERE ID = ? ";
		args.add(newExam.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<NewExam>(NewExam.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM NEWEXAM ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(NewExam newExam) {
		if(newExam.getIndex_sort() != null) {
			SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExam);
			String sql = "INSERT INTO NEWEXAM (ID, TITLE, AREA, CATEGORY, IMAGE, PHOTO, "
					   + "BEGIN_DATE, END_DATE, CLICK_RATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, "
					   + "CONTENT, ENCRYPTURL, PRODUCT_CATEGORY) VALUES (:id, :title, :area, :category, :image, :photo, "
					   + ":begin_date, :end_date, 0, :create_by, now(), :update_by, now(), "
					   + ":content, :encrypturl, :product_category) ";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
		}else {
			SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExam);
			String sql = "INSERT INTO NEWEXAM (ID, TITLE, AREA, CATEGORY, IMAGE, PHOTO,  "
					   + "BEGIN_DATE, END_DATE, CLICK_RATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, "
					   + "CONTENT, ENCRYPTURL, PRODUCT_CATEGORY) VALUES (:id, :title, :area, :category, :image, :photo, "
					   + ":begin_date, :end_date, 0, :create_by, now(), :update_by, now(), "
					   + ":content, :encrypturl, :product_category)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
		}

	}

	
	@Override
	public void update(NewExam newExam) {
			SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExam);
			String sql = "UPDATE NEWEXAM SET TITLE = :title, CATEGORY = :category, "
					   + "IMAGE = :image, PHOTO = :photo, PRODUCT_CATEGORY = :product_category, BEGIN_DATE = :begin_date, END_DATE = :end_date, UPDATE_BY = :update_by, "
					   + "UPDATE_DATE = now(), CONTENT = :content WHERE ID = :id ";
			postgresqlJdbcNameTemplate.update(sql, parameter);
	}
	
	@Override
	public void updateClickRate(NewExam newExam) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE NEWEXAM SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(newExam.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());

	}

	@Override
	public void delete(NewExam newExam,Integer id) {
		String sql = "DELETE FROM NEWEXAM WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql,new Object[] {id});
	}

	
//	===========================
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from newexam";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(NewExam newExam) {
		String sql = " INSERT into newexam (id,title,area,category,image,begin_date,end_date,click_rate,create_by,create_date,update_by,update_date,content,photo,encrypturl,product_category) VALUES (:id,:title,:area,:category,:image,:begin_date,:end_date,:click_rate,:create_by,:create_date,:update_by,:update_date,:content,:photo,:encrypturl,:product_category)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET title = :title,area = :area,category = :category,image = :image,begin_date = :begin_date,end_date = :end_date,click_rate = :click_rate,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,content = :content,photo = :photo,encrypturl = :encrypturl,product_category = :product_category "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(newExam);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);		
	}
	
	
	
}
