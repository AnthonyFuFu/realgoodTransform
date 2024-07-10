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

import com.tkb.realgoodTransform.dao.WinnerCategoryDao;
import com.tkb.realgoodTransform.model.WinnerCategory;



@Repository
public class WinnerCategoryDaoImpl implements WinnerCategoryDao {
	
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
	public List<WinnerCategory> getSubList(WinnerCategory winnerCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT *FROM WINNER_CATEGORY WHERE PARENT_ID = ? ";
		args.add(winnerCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class),args.toArray());
	}

	@Override
	public List<WinnerCategory> getLayerList(String layer, WinnerCategory winnerCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM WINNER_CATEGORY WHERE LAYER = ? AND PARENT_ID = ? ORDER BY sort";
		args.add(layer);
		args.add(winnerCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class),args.toArray());
	}

	@Override
	public List<WinnerCategory> getLayerList(String layer) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM WINNER_CATEGORY WHERE LAYER = ? ";
		args.add(layer);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class),args.toArray());
	}

	@Override
	public List<WinnerCategory> getList(int pageCount, int pageStart, WinnerCategory winnerCategory) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM WINNER_CATEGORY WHERE LAYER = ? ");
		if(winnerCategory.getLayer() == null) {
			winnerCategory.setLayer("1");
		}
		args.add(winnerCategory.getLayer());
		if(winnerCategory.getParent_id() != null) {
			sql.append("AND PARENT_ID = ? ");
			args.add(winnerCategory.getParent_id());
		}
		sql.append("ORDER BY SORT LIMIT ? OFFSET ?");
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class),args.toArray());
	}

	@Override
	public Integer getCount(WinnerCategory winnerCategory) {
		List<Object>args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM WINNER_CATEGORY WHERE LAYER = ? ");
		if(winnerCategory.getLayer() == null) {
			winnerCategory.setLayer("1");
		}
		args.add(winnerCategory.getLayer());
		if(winnerCategory.getParent_id() != null) {
			sql.append("AND PARENT_ID = ?");
			args.add(winnerCategory.getParent_id());
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public WinnerCategory getData(WinnerCategory winnerCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM WINNER_CATEGORY WHERE ID = ? ";
		args.add(winnerCategory.getId());
		try {
			return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class),args.toArray());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public WinnerCategory checkRepeat(WinnerCategory winnerCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM WINNER_CATEGORY WHERE NAME = ? AND LAYER = ?  AND PARENT_ID = ?";
		args.add(winnerCategory.getName());
		args.add(winnerCategory.getLayer());
		args.add(winnerCategory.getParent_id());
		List<WinnerCategory>list = postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class), args.toArray());
		return list.size() ==0 ? null : list.get(0);
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM WINNER_CATEGORY ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(WinnerCategory winnerCategory) {
		addsort(winnerCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(winnerCategory);
		String sql = "INSERT INTO WINNER_CATEGORY (ID, PARENT_ID, NAME,CATEGORY_CODE, LAYER, SORT, CREATE_BY, CREATE_DATE, "
				   + "UPDATE_BY, UPDATE_DATE) VALUES (:id, :parent_id, :name, :category_code, :layer, :sort, :create_by, now(), :update_by, now() )";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}
	
	public void addsort (WinnerCategory winnerCategory) {
		List<Object> args = new ArrayList<>();
		String sql  = "UPDATE WINNER_CATEGORY SET SORT = SORT + 1 WHERE SORT >= ? AND PARENT_ID = ?";
		args.add(winnerCategory.getSort());
		args.add(winnerCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql , args.toArray());
	}

	@Override
	public void update(WinnerCategory winnerCategory) {
		sort(winnerCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(winnerCategory);
		String sql = "UPDATE WINNER_CATEGORY SET NAME = :name,CATEGORY_CODE= :category_code, SORT = :sort, UPDATE_BY = :update_by, "
				   + "UPDATE_DATE = now() WHERE ID = :id";
		postgresqlJdbcNameTemplate.update(sql, parameter);

	}
	
	public void sort (WinnerCategory winnerCategory) {
		String sql = "SELECT COUNT(*) FROM WINNER_CATEGORY WHERE SORT = ? AND PARENT_ID = ? ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, new Object[] {winnerCategory.getSort(), winnerCategory.getParent_id()});
		if(count > 0) {
			sql = "SELECT SORT FROM WINNER_CATEGORY WHERE ID = ? AND PARENT_ID = ? ";
			WinnerCategory oldWinnerCategory = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class), new Object[] {winnerCategory.getId(), winnerCategory.getParent_id()});
			if(winnerCategory.getSort() <  oldWinnerCategory.getSort()) {
				sql = "UPDATE WINNER_CATEGORY SET SORT = SORT + 1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {oldWinnerCategory.getSort(), winnerCategory.getSort(), winnerCategory.getParent_id()});
			}else if (winnerCategory.getSort() > oldWinnerCategory.getSort()) {
				sql = "UPDATE WINNER_CATEGORY SET SORT = SORT - 1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {winnerCategory.getSort(), oldWinnerCategory.getSort(), winnerCategory.getParent_id()});
			}
		}
		sql = "UPDATE WINNER_CATEGORY SET SORT = ? WHERE ID = ? AND PARENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {winnerCategory.getSort(), winnerCategory.getId(), winnerCategory.getParent_id()});
	}
	
	@Override
	public void delete(WinnerCategory winnerCategory, Integer id) {
		deleteSort(winnerCategory);
		String sql = "DELETE FROM WINNER_CATEGORY WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql , new Object[] {id});
	}
	
	public void deleteSort(WinnerCategory winnerCategory) {
		String sql1 = "SELECT SORT FROM WINNER_CATEGORY WHERE ID = ? ";
		WinnerCategory oldWinnerCategory = postgresqlJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<WinnerCategory>(WinnerCategory.class),new Object[] {winnerCategory.getId()});
		String sql = "UPDATE WINNER_CATEGORY SET SORT = SORT - 1 WHERE SORT > ? AND PARENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {oldWinnerCategory.getSort(), winnerCategory.getParent_id()});
	}

	@Override
	public void resetSort(WinnerCategory winnerCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM WINNER_CATEGORY WHERE PARENT_ID = ? ";
		args.add(winnerCategory.getParent_id());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		List<Object>resetArgs = new ArrayList<Object>();
		sql = "SELECT SORT FROM WINNER_CATEGORY WHERE PARENT_ID = ? ORDER BY SORT ";
		resetArgs.add(winnerCategory.getParent_id());
		List<Integer>sortList = postgresqlJdbcTemplate.queryForList(sql,Integer.class, resetArgs.toArray());
		for(int i = 0; i<count; i++) {
			String resetSql = "UPDATE WINNER_CATEGORY SET SORT = ? WHERE SORT = ? AND PARENT_ID = ? ";
			postgresqlJdbcTemplate.update(resetSql, new Object[] {i+1, sortList.get(i), winnerCategory.getParent_id()});
		}

	}

	@Override
	public String getCategoryName(String category, WinnerCategory winnerCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT NAME FROM WINNER_CATEGORY WHERE ID = ? AND PARENT_ID = ? ";
		args.add(Integer.valueOf(category));
		args.add(winnerCategory.getParent_id());
		return postgresqlJdbcTemplate.queryForObject(sql,String.class,args.toArray());
	}

	@Override
	public Integer deleteCheck(WinnerCategory winerCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM WINNER WHERE CAST(CATEGORY AS INTEGER) = ? ";
		args.add(winerCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	
//	=========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from winner_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(WinnerCategory winerCategory) {
		String sql = " INSERT into winner_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort,category_code) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort,:category_code)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id,name = :name,layer = :layer,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,sort = :sort,category_code = :category_code "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(winerCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
