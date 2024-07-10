package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.NewExamCategoryDao;
import com.tkb.realgoodTransform.model.NewExamCategory;


@Repository
public class NewExamCategoryDaoImpl implements NewExamCategoryDao {
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
	public Integer getCount(NewExamCategory newExamCategory) {
		List<Object> args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM newexam_category WHERE layer = ? ");
		if(newExamCategory.getLayer() == null) newExamCategory.setLayer("1");
		args.add(newExamCategory.getLayer());
		
		if(newExamCategory.getParent_id() != null) {
			sql.append(" AND parent_id = ? ");
			args.add(newExamCategory.getParent_id());
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public List<NewExamCategory> checkNewExamCategory(NewExamCategory newExamCategory) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamCategory);
		String sql = "SELECT * FROM newexam_category WHERE name=:name AND layer=:layer ";
		return postgresqlJdbcNameTemplate.query(sql, parameter, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class));
	}

	@Override
	public void add(NewExamCategory newExamCategory) {
		addSort(newExamCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamCategory);
		String sql = "INSERT INTO newexam_category(id,parent_id, name, layer, sort, create_by, create_date) "
				+ "VALUES(:id, :parent_id, :name, :layer, :sort, :create_by, now()) ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	public void addSort(NewExamCategory newExamCategory) {
		List<Object> args = new ArrayList<>();
		String sql = "UPDATE newexam_category SET sort = sort + 1 WHERE sort >= ? AND parent_id = ? ";
		args.add(newExamCategory.getSort());
		args.add(newExamCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public List<NewExamCategory> getList(int pageCount, int pageStart, NewExamCategory newExamCategory) {
		List<Object> args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM newexam_category WHERE LAYER = ?");
		if(newExamCategory.getLayer() == null) newExamCategory.setLayer("1");
		args.add(newExamCategory.getLayer());
		
		if(newExamCategory.getParent_id() != null) {
			sql.append(" AND parent_id = ? ");
			args.add(newExamCategory.getParent_id());
		}
		sql.append(" ORDER BY sort")
		   .append(" LIMIT ? OFFSET ? ");
		args.add(pageCount);
		args.add(pageStart);
		
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class), args.toArray());
	}

	@Override
	public List<NewExamCategory> getSubList(NewExamCategory newExamCategory) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamCategory);
		String sql = "SELECT * FROM newexam_category WHERE parent_id = :parent_id";
		return postgresqlJdbcNameTemplate.query(sql, parameter, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class));
	}

	@Override
	public void delete(NewExamCategory newExamCategory) {
		deleteSort(newExamCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamCategory);
		String sql = "DELETE FROM newexam_category WHERE id = :id";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}
	
	public void deleteSort(NewExamCategory newExamCategory) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamCategory);
		String sql = "SELECT sort FROM newexam_category WHERE id = :id ";
		NewExamCategory toDeleteNewExamCategory = postgresqlJdbcNameTemplate.queryForObject(sql, parameter, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class));
		
		List<Object> args = new ArrayList<>();
		String sql2 = "UPDATE newexam_category SET sort = sort - 1 WHERE sort > ? AND parent_id = ? ";
		args.add(toDeleteNewExamCategory.getSort());
		args.add(toDeleteNewExamCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql2, args.toArray());
	}

	@Override
	public void resetSort(Integer parent_id) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM newexam_category WHERE parent_id = ? ";
		args.add(parent_id);
		Integer count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		
		String sql2 = "SELECT sort FROM newexam_category WHERE parent_id = ? ORDER BY sort ASC ";
		for(int i=0 ; i<count ; i++) {
			List<NewExamCategory> query = postgresqlJdbcTemplate.query(sql2, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class), args.toArray());
			List<Object> updateArgs = new ArrayList<>();
			String updateSql = "UPDATE newexam_category SET sort = ? WHERE sort = ? AND parent_id = ? ";
			updateArgs.add(i+1);
			updateArgs.add(query.get(i).getSort());
			updateArgs.add(parent_id);
			postgresqlJdbcTemplate.update(updateSql, updateArgs.toArray());
		}
	}

	@Override
	public NewExamCategory getData(Integer id) {
		String sql = "SELECT * FROM newexam_category WHERE id = ? ";
		try {
			return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class), id);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void update(NewExamCategory newExamCategory) {
		updateSort(newExamCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(newExamCategory);
		String sql = "UPDATE newexam_category SET name = :name, sort = :sort, update_by = :update_by, "
				+ "update_date = now() WHERE id = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	private void updateSort(NewExamCategory newExamCategory) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM newexam_category WHERE sort = ? AND parent_id = ? ";
		args.add(newExamCategory.getSort());
		args.add(newExamCategory.getParent_id());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		//若原有排序號碼已存在
		if (count > 0) {
			List<Object> updateArgs = new ArrayList<>();
			sql = "SELECT sort FROM newexam_category WHERE id = ? AND parent_id = ? ";
			updateArgs.add(newExamCategory.getId());
			updateArgs.add(newExamCategory.getParent_id());
			NewExamCategory oldNewExamCategory = postgresqlJdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class), updateArgs.toArray());
			if (newExamCategory.getSort() < oldNewExamCategory.getSort()) {
				// 將原有排序號碼與新排序號碼中間之號碼全部+1
				List<Object> updateArgs2 = new ArrayList<>();
				sql = "UPDATE newexam_category SET sort = sort + 1 WHERE ? >= sort AND sort >= ? AND parent_id = ? ";
				updateArgs2.add(oldNewExamCategory.getSort());
				updateArgs2.add(newExamCategory.getSort());
				updateArgs2.add(newExamCategory.getParent_id());
				postgresqlJdbcTemplate.update(sql, updateArgs2.toArray());
			} else if (newExamCategory.getSort() > oldNewExamCategory.getSort()) {
				// 將原有排序號碼與新排序號碼中間之號碼全部-1
				List<Object> updateArgs3 = new ArrayList<>();
				sql = "UPDATE newexam_category SET sort = sort - 1 WHERE ? >= sort AND sort >= ? AND parent_id = ? ";
				updateArgs3.add(newExamCategory.getSort());
				updateArgs3.add(oldNewExamCategory.getSort());
				updateArgs3.add(newExamCategory.getParent_id());
				postgresqlJdbcTemplate.update(sql, updateArgs3.toArray());
			}
		}
		List<Object> updateArgs4 = new ArrayList<>();
		sql = "UPDATE newexam_category SET sort = ? WHERE id = ? AND parent_id = ? ";
		updateArgs4.add(newExamCategory.getSort());
		updateArgs4.add(newExamCategory.getId());
		updateArgs4.add(newExamCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql, updateArgs4.toArray());
	}

	@Override
	public List<NewExamCategory> getLayerList(String layer, NewExamCategory newExamCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM NEWEXAM_CATEGORY WHERE LAYER = ? AND PARENT_ID = ? ORDER BY SORT ";
		args.add(layer);
		args.add(newExamCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM NEWEXAM_CATEGORY ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class)+1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public Integer deleteCheck(NewExamCategory newExamCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM NEWEXAM WHERE CAST(CATEGORY AS INTEGER) = ?";
		args.add(newExamCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}
	
	@Override
	public List<Map<String, Object>> getNewExamMenuMenu(NewExamCategory newExamCategory) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM NEWEXAM_CATEGORY WHERE PARENT_ID = ? AND LAYER = ?  ORDER BY SORT";
		
		args.add(newExamCategory.getParent_id());
		args.add(newExamCategory.getLayer());
		
		return postgresqlJdbcTemplate.queryForList(sql, args.toArray());
	}

	
	
//	===========================
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from newexam_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(NewExamCategory newExamCategory) {
		String sql = " INSERT into newexam_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id, name = :name, layer = :layer,create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(newExamCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);		
	}
	

}
