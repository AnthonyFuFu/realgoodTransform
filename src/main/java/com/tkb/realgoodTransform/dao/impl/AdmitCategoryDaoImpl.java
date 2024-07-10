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

import com.tkb.realgoodTransform.dao.AdmitCategoryDao;
import com.tkb.realgoodTransform.model.AdmitCategory;



/**
 * 金榜類別Dao實作類
 * 
 * @author Ken
 * @version 創建時間：2016-04-26
 */
@Repository
public class AdmitCategoryDaoImpl implements AdmitCategoryDao {

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
	public List<AdmitCategory> getSubList(AdmitCategory admitCategory) {

		String sql = " SELECT * FROM ADMIT_CATEGORY ";

		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<AdmitCategory>(AdmitCategory.class));

	}

	@Override
	public List<AdmitCategory> getLayerList(String layer, AdmitCategory admitCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT * FROM ADMIT_CATEGORY WHERE LAYER = ?  ORDER BY SORT";
		args.add(layer);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<AdmitCategory>(AdmitCategory.class),
				args.toArray());

	}

	@Override
	public List<AdmitCategory> getList(int pageCount, int pageStart, AdmitCategory admitCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT * FROM ADMIT_CATEGORY WHERE LAYER = ? ";

		if (admitCategory.getLayer() == null) {
			admitCategory.setLayer("1");
		}

		args.add(admitCategory.getLayer());

		sql += " ORDER BY SORT LIMIT ? OFFSET ?";
		args.add(pageCount);
		args.add(pageStart);

		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<AdmitCategory>(AdmitCategory.class),
				args.toArray());

	}

	@Override
	public Integer getCount(AdmitCategory admitCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM ADMIT_CATEGORY WHERE LAYER = ? ";

		if (admitCategory.getLayer() == null) {
			admitCategory.setLayer("1");
		}

		args.add(admitCategory.getLayer());

		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public AdmitCategory getData(AdmitCategory admitCategory) {
		String sql = " SELECT * FROM ADMIT_CATEGORY WHERE ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<AdmitCategory>(AdmitCategory.class),new Object[] { admitCategory.getId() });
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM ADMIT_CATEGORY ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;

	}

	@Override
	public void add(AdmitCategory admitCategory) {
		addSort(admitCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admitCategory);
		String sql = " INSERT INTO ADMIT_CATEGORY(ID, PARENT_ID, NAME, LAYER, SORT, CREATE_BY, CREATE_DATE,"
				+ " UPDATE_BY, UPDATE_DATE)VALUES(:id, :parent_id, :name, :layer, :sort, :create_by, now(), :update_by, now()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void update(AdmitCategory admitCategory) {
		sort(admitCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admitCategory);
		String sql = " UPDATE ADMIT_CATEGORY SET NAME = :name, SORT = :sort, UPDATE_BY = :update_by, UPDATE_DATE = now()"
				+ " WHERE ID = :id ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	public void addSort(AdmitCategory admitCategory) {
		String sql = "UPDATE ADMIT_CATEGORY SET SORT = SORT +1 WHERE SORT >= ?";
		postgresqlJdbcTemplate.update(sql, new Object[] { admitCategory.getSort()});

	}

	public void sort(AdmitCategory admitCategory) {
		String sql = "SELECT COUNT(*) FROM ADMIT_CATEGORY WHERE SORT = ?";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class,
				new Object[] { admitCategory.getSort()});

		// 若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT SORT FROM ADMIT_CATEGORY WHERE ID = ?";

			AdmitCategory oldAdmitCategory = postgresqlJdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper<AdmitCategory>(AdmitCategory.class),
					new Object[] { admitCategory.getId()});

			if (admitCategory.getSort() < oldAdmitCategory.getSort()) {

				// 將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE ADMIT_CATEGORY SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ?";
				postgresqlJdbcTemplate.update(sql, new Object[] { oldAdmitCategory.getSort(), admitCategory.getSort()});

			} else if (admitCategory.getSort() > oldAdmitCategory.getSort()) {

				// 將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE ADMIT_CATEGORY SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ?";
				postgresqlJdbcTemplate.update(sql, new Object[] { admitCategory.getSort(), oldAdmitCategory.getSort()});
			}
		}
		sql = "UPDATE ADMIT_CATEGORY SET SORT = ? WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql,
				new Object[] { admitCategory.getSort(), admitCategory.getId()});
	}

	public void updateEditor(AdmitCategory admitCategory) {
		String sql = "UPDATE ADMIT_CATEGORY SET UPDATE_BY = ?, UPDATE_DATE = now()";
		postgresqlJdbcTemplate.update(sql, new Object[] { admitCategory.getUpdate_by() });
	}

	@Override
	public void resetSort(AdmitCategory admitCategory) {
		String sql = "SELECT COUNT(*) FROM ADMIT_CATEGORY";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class);

		sql = "SELECT SORT FROM ADMIT_CATEGORY ORDER BY SORT ASC";
		List<Integer> sortList = postgresqlJdbcTemplate.queryForList(sql, Integer.class);

		for (int i = 0; i < count; i++) {
			String sql2 = "UPDATE ADMIT_CATEGORY SET SORT = ? WHERE SORT = ? ";
			postgresqlJdbcTemplate.update(sql2, new Object[] { i + 1, sortList.get(i) });
		}
	}

	public void deleteSort(AdmitCategory admitCategory) {
		String sql1 = "SELECT SORT FROM ADMIT_CATEGORY WHERE ID = ?";

		AdmitCategory oldAdmitCategory = postgresqlJdbcTemplate.queryForObject(sql1,
				new BeanPropertyRowMapper<AdmitCategory>(AdmitCategory.class), new Object[] { admitCategory.getId() });

		String sql = "UPDATE ADMIT_CATEGORY SET SORT = SORT -1 WHERE SORT > ?";

		postgresqlJdbcTemplate.update(sql, new Object[] { oldAdmitCategory.getSort()});

	}

	@Override
	public void delete(AdmitCategory admitCategory, Integer id) {
		deleteSort(admitCategory);
		String sql = " DELETE FROM ADMIT_CATEGORY WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });
	}

	@Override
	public Integer getCountByName(String categoryName) {
		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) AS COUNT FROM ADMIT_CATEGORY WHERE NAME = ? ";

		args.add(categoryName);

		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	
//	=========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from admit_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(AdmitCategory admitCategory) {
		String sql = " INSERT into admit_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id,name = :name,layer = :layer,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(admitCategory);
	
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
