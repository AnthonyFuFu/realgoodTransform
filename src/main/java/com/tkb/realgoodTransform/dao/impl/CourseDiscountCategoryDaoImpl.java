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

import com.tkb.realgoodTransform.dao.CourseDiscountCategoryDao;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;



@Repository
public class CourseDiscountCategoryDaoImpl implements CourseDiscountCategoryDao {
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
	public List<CourseDiscountCategory> getSubList(CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM COURSE_DISCOUNT_CATEGORY WHERE PARENT_ID = ? ";
		args.add(courseDiscountCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),args.toArray());
	}

	@Override
	public List<CourseDiscountCategory> getLayerList(String layer, CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM COURSE_DISCOUNT_CATEGORY WHERE LAYER = ? AND PARENT_ID = ? ORDER BY SORT ";
		args.add(layer);
		args.add(courseDiscountCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),args.toArray());
	}

	@Override
	public List<CourseDiscountCategory> getList(int pageCount, int pageStart,
			CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT * FROM COURSE_DISCOUNT_CATEGORY WHERE LAYER = ? ");
		if(courseDiscountCategory.getLayer() == null) {
			courseDiscountCategory.setLayer("1");
		}
		args.add(courseDiscountCategory.getLayer());
		if(courseDiscountCategory.getParent_id() != null) {
			sql.append("AND PARENT_ID = ? ");
			args.add(courseDiscountCategory.getParent_id());
		}
		sql.append("ORDER BY SORT LIMIT ? OFFSET ? ");
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),args.toArray());
	}

	@Override
	public Integer getCount(CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM COURSE_DISCOUNT_CATEGORY WHERE LAYER = ? ");
		if(courseDiscountCategory.getLayer() == null) {
			courseDiscountCategory.setLayer("1");
		}
		args.add(courseDiscountCategory.getLayer());
		if(courseDiscountCategory.getParent_id() != null) {
			sql.append("AND PARENT_ID = ? ") ;
			args.add(courseDiscountCategory.getParent_id());
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public CourseDiscountCategory getData(CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM COURSE_DISCOUNT_CATEGORY WHERE ID = ?";
		args.add(courseDiscountCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM COURSE_DISCOUNT_CATEGORY ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(CourseDiscountCategory courseDiscountCategory) {
		addSort(courseDiscountCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(courseDiscountCategory);
		String sql = "INSERT INTO COURSE_DISCOUNT_CATEGORY (ID, PARENT_ID, NAME, LAYER, SORT, CREATE_BY, CREATE_DATE, "
				   + "UPDATE_BY, UPDATE_DATE ) VALUES (:id, :parent_id, :name, :layer, :sort, :create_by, now(), "
				   + ":update_by, now()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	public void addSort(CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "UPDATE COURSE_DISCOUNT_CATEGORY SET SORT = SORT + 1 WHERE SORT >= ? AND PARENT_ID = ? ";
		args.add(courseDiscountCategory.getSort());
		args.add(courseDiscountCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql,args.toArray());
	}
	
	@Override
	public void update(CourseDiscountCategory courseDiscountCategory) {
		sort(courseDiscountCategory);
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("UPDATE COURSE_DISCOUNT_CATEGORY SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ");
		args.add(courseDiscountCategory.getUpdate_by());
		if(courseDiscountCategory.getName() != null) {
			sql.append("NAME = ?, ");
			args.add(courseDiscountCategory.getName());
		}
		if(courseDiscountCategory.getSort() != null) {
			sql.append("SORT = ? ");
			args.add(courseDiscountCategory.getSort());
		}
		sql.append("WHERE ID = ? ");
		args.add(courseDiscountCategory.getId());
		postgresqlJdbcTemplate.update(sql.toString(),args.toArray());
	}
	
	public void sort(CourseDiscountCategory courseDiscountCategory) {
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT_CATEGORY WHERE SORT = ? AND PARENT_ID = ? ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, new Object[] {courseDiscountCategory.getSort(), courseDiscountCategory.getParent_id()});
		if(count > 0) {
			sql = "SELECT SORT FROM COURSE_DISCOUNT_CATEGORY WHERE ID = ? AND PARENT_ID = ? ";
			CourseDiscountCategory oldCourseDiscountCategory = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),new Object[] {courseDiscountCategory.getId(),courseDiscountCategory.getParent_id()});
			if(courseDiscountCategory.getSort() > oldCourseDiscountCategory.getSort()) {
				sql = "UPDATE COURSE_DISCOUNT_CATEGORY SET SORT = SORT - 1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql , new Object[] {courseDiscountCategory.getSort(), oldCourseDiscountCategory.getSort(), courseDiscountCategory.getParent_id()});
			}else if(courseDiscountCategory.getSort() < oldCourseDiscountCategory.getSort()) {
				sql = "UPDATE COURSE_DISCOUNT_CATEGORY SET SORT = SORT + 1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql , new Object[] {oldCourseDiscountCategory.getSort(), courseDiscountCategory.getSort(), courseDiscountCategory.getParent_id()});
			}
		}
		sql = "UPDATE COURSE_DISCOUNT_CATEGORY SET SORT = ? WHERE ID = ? AND PARENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql , new Object[] {courseDiscountCategory.getSort(), courseDiscountCategory.getId(), courseDiscountCategory.getParent_id()});
	}
	
	@Override
	public void delete(CourseDiscountCategory courseDiscountCategory, Integer id) {
		deleteSort(courseDiscountCategory);
		String sql = "DELETE FROM COURSE_DISCOUNT_CATEGORY WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}
	
	public void deleteSort(CourseDiscountCategory courseDiscountCategory) {
		String sql = "SELECT SORT FROM COURSE_DISCOUNT_CATEGORY WHERE ID = ? ";
		CourseDiscountCategory oldCourseDiscountCategory = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),new Object[] {courseDiscountCategory.getId()});
		String deleteSql = "UPDATE COURSE_DISCOUNT_CATEGORY SET SORT = SORT - 1 WHERE SORT >= ? AND PARENT_ID = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(oldCourseDiscountCategory.getSort());
		args.add(courseDiscountCategory.getParent_id());
		postgresqlJdbcTemplate.update(deleteSql, args.toArray());
	}
	
	@Override
	public String checkCourseDiscountCategory(CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM COURSE_DISCOUNT_CATEGORY WHERE NAME = ? AND LAYER = ? ";
		args.add(courseDiscountCategory.getName());
		args.add(courseDiscountCategory.getLayer());
		List<CourseDiscountCategory>list = postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<CourseDiscountCategory>(CourseDiscountCategory.class),args.toArray());
		return list.size() == 0 ? null : list.get(0).getName();
	}

	@Override
	public void resetSort(CourseDiscountCategory courseDiscountCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT_CATEGORY WHERE PARENT_ID = ? ";
		args.add(courseDiscountCategory.getParent_id());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		List<Object>resetArgs = new ArrayList<Object>();
		sql = "SELECT SORT FROM COURSE_DISCOUNT_CATEGORY WHERE PARENT_ID = ? ORDER BY SORT ";
		resetArgs.add(courseDiscountCategory.getParent_id());
		List<Integer>sortList = postgresqlJdbcTemplate.queryForList(sql,Integer.class,resetArgs.toArray());
		for(int i = 0; i<count; i++) {
			String resetSql = "UPDATE COURSE_DISCOUNT_CATEGORY SET SORT = ? WHERE SORT = ? AND PARENT_ID = ? ";
			postgresqlJdbcTemplate.update(resetSql,new Object[] {i+1, sortList.get(i),courseDiscountCategory.getParent_id()});
		}

	}

	@Override
	public Integer deleteCheck(CourseDiscountCategory courseDiscountCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT WHERE CAST(CATEGORY AS INTEGER) = ? ";
		args.add(courseDiscountCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	
//	==============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from course_discount_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(CourseDiscountCategory courseDiscountCategory) {
		String sql = " INSERT into course_discount_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id,name = :name,layer = :layer,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(courseDiscountCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
