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

import com.tkb.realgoodTransform.dao.LecturesCategoryDao;
import com.tkb.realgoodTransform.model.LecturesCategory;



@Repository
public class LecturesCategoryDaoImpl implements LecturesCategoryDao {

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
	public List<LecturesCategory> getSubList(LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_CATEGORY WHERE PARENT_ID = ?";
		args.add(lecturesCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),args.toArray());
	}

	@Override
	public List<LecturesCategory> getLayerList(String layer, LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_CATEGORY WHERE LAYER = ? AND PARENT_ID = ? ORDER BY SORT";
		args.add(layer);
		args.add(lecturesCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),args.toArray());
	}

	@Override
	public List<LecturesCategory> getList(int pageCount, int pageStart, LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM LECTURES_CATEGORY WHERE LAYER = ? ");
		if(lecturesCategory.getLayer() == null) {
			lecturesCategory.setLayer("1");
		}
		args.add(lecturesCategory.getLayer());
		if(lecturesCategory.getParent_id() != null) {
			sql.append("AND PARENT_ID = ? ");
			args.add(lecturesCategory.getParent_id());
		}
		sql.append("ORDER BY SORT LIMIT ?  OFFSET ? ");
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class), args.toArray());
	}

	@Override
	public Integer getCount(LecturesCategory lecturesCategory) {
		List<Object> args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM LECTURES_CATEGORY WHERE LAYER = ? ");
		if(lecturesCategory.getLayer() == null) {
			lecturesCategory.setLayer("1");
		}
		args.add(lecturesCategory.getLayer());
		if(lecturesCategory.getParent_id() != null) {
			sql.append("AND PARENT_ID = ?");
			args.add(lecturesCategory.getParent_id());
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public LecturesCategory getData(LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_CATEGORY WHERE ID = ?";
		args.add(lecturesCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM LECTURES_CATEGORY ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(LecturesCategory lecturesCategory) {
		addSort(lecturesCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lecturesCategory);
		String sql = "INSERT INTO LECTURES_CATEGORY (ID, PARENT_ID, NAME, LAYER, SORT, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE ) "
				   + "VALUES(:id, :parent_id, :name, :layer, :sort, :create_by, now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}
	
	public void addSort(LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE LECTURES_CATEGORY SET SORT = SORT + 1 WHERE SORT >= ? AND PARENT_ID = ? ";
		args.add(lecturesCategory.getSort());
		args.add(lecturesCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void update(LecturesCategory lecturesCategory) {
		sort(lecturesCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lecturesCategory);
		String sql = "UPDATE LECTURES_CATEGORY SET NAME = :name, SORT = :sort, UPDATE_BY = :update_by, UPDATE_DATE = now() WHERE ID = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);

	}
	
	public void sort(LecturesCategory lecturesCategory) {
		String sql = "SELECT COUNT(*) FROM LECTURES_CATEGORY WHERE SORT = ? AND PARENT_ID = ? ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, new Object[] {lecturesCategory.getSort(),lecturesCategory.getParent_id()});
		if(count > 0) {
			sql = "SELECT SORT FROM LECTURES_CATEGORY WHERE ID = ? AND PARENT_ID = ? ";
			LecturesCategory oldLecturesCategory = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),new Object[] {lecturesCategory.getId(),lecturesCategory.getParent_id()});
			if(lecturesCategory.getSort() < oldLecturesCategory.getSort()) {
				sql = "UPDATE LECTURES_CATEGORY SET SORT = SORT + 1 WHERE SORT BETWEEN ? AND  ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql,new Object[] {lecturesCategory.getSort(),oldLecturesCategory.getSort(),lecturesCategory.getParent_id()});
			}else if(lecturesCategory.getSort() > oldLecturesCategory.getSort()) {
				sql = "UPDATE LECTURES_CATEGORY SET SORT = SORT - 1 WHERE SORT BETWEEN ? AND ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql,new Object[] {oldLecturesCategory.getSort(),lecturesCategory.getSort(),lecturesCategory.getParent_id()});
			}
		}
		sql = "UPDATE LECTURES_CATEGORY SET SORT = ? WHERE ID = ? AND PARENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {lecturesCategory.getSort(),lecturesCategory.getId(),lecturesCategory.getParent_id()});
	}

	@Override
	public void delete(LecturesCategory lecturesCategory, Integer id) {
		deleteSort(lecturesCategory);
		String sql = "DELETE FROM LECTURES_CATEGORY WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql,new Object[] {id});

	}
	
	public void deleteSort(LecturesCategory lecturesCategory) {
		String sql1 = "SELECT SORT FROM LECTURES_CATEGORY WHERE ID = ? ";
		LecturesCategory oldLecturesCategory = postgresqlJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),new Object[] {lecturesCategory.getId()});
		String sql = "UPDATE LECTURES_CATEGORY SET SORT = SORT -1 WHERE SORT > ? AND PARENT_ID = ?";
		postgresqlJdbcTemplate.update(sql,new Object[] {oldLecturesCategory.getSort(), lecturesCategory.getParent_id()});
	}

	@Override
	public String checkLecturesCategory(LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_CATEGORY WHERE NAME = ? AND LAYER = ? AND PARENT_ID = ? ";
		args.add(lecturesCategory.getName());
		args.add(lecturesCategory.getLayer());
		args.add(lecturesCategory.getParent_id());
		List<LecturesCategory>list = postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),args.toArray());
		return list.size() == 0 ? null : list.get(0).getName();
	}

	@Override
	public void resetSort(LecturesCategory lecturesCategory) {
		List<Object> args = new ArrayList<>();
		List<Object> args2 = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM LECTURES_CATEGORY WHERE PARENT_ID = ? ";
		args.add(lecturesCategory.getParent_id());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		sql = "SELECT SORT FROM  LECTURES_CATEGORY WHERE PARENT_ID = ? ORDER BY SORT ASC ";
		args2.add(lecturesCategory.getParent_id());
		List<Integer>sortList = postgresqlJdbcTemplate.queryForList(sql,Integer.class, args2.toArray());
		for(int i=0;i<count;i++) {
			String sql2 = "UPDATE LECTURES_CATEGORY SET SORT = ? WHERE SORT = ? AND PARENT_ID = ?";
			postgresqlJdbcTemplate.update(sql2, new Object[] {i+1, sortList.get(i), lecturesCategory.getParent_id()});
		}
	
		}

	@Override
	public List<LecturesCategory> getFrontList(LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_CATEGORY WHERE ";
		if(lecturesCategory.getParent_id() == null ) {
			sql += "LAYER = '2' ";
		}else {
			sql += "PARENT_ID = ? ";
		args.add(lecturesCategory.getParent_id());
		}
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),args.toArray());
	}

	@Override
	public List<LecturesCategory> getFrontLayerList(String layer) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_CATEGORY WHERE LAYER = ?  ORDER BY SORT ";
		args.add(layer);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesCategory>(LecturesCategory.class),args.toArray());
	}

	@Override
	public Integer deleteCheck(LecturesCategory lecturesCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM LECTURES WHERE CAST(CATEGORY AS INTEGER) = ? ";
		args.add(lecturesCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql,Integer.class, args.toArray());
	}

	
//	====================
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from lectures_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(LecturesCategory lecturesCategory) {
		String sql = " INSERT into lectures_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id,name = :name,layer = :layer,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lecturesCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}


