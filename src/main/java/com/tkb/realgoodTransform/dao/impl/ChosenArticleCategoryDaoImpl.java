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

import com.tkb.realgoodTransform.dao.ChosenArticleCategoryDao;
import com.tkb.realgoodTransform.model.ChosenArticleCategory;
import com.tkb.realgoodTransform.model.NewExamCategory;



/**
 * 精選文章類別Dao實作類
 * 
 * @author
 * @version 創建時間：2016-07-06
 */
@Repository
public class ChosenArticleCategoryDaoImpl implements ChosenArticleCategoryDao {

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
	public List<ChosenArticleCategory> getSubList(ChosenArticleCategory chosenArticleCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT * FROM CHOSEN_ARTICLE_CATEGORY WHERE PARENT_ID = ? ";

		args.add(chosenArticleCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<ChosenArticleCategory>(ChosenArticleCategory.class), args.toArray());

	}

	@Override
	public List<ChosenArticleCategory> getLayerList(String layer, ChosenArticleCategory chosenArticleCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT * FROM CHOSEN_ARTICLE_CATEGORY WHERE PARENT_ID = ? AND LAYER = ?  ORDER BY SORT";

		args.add(chosenArticleCategory.getParent_id());
		args.add(layer);
		
		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<ChosenArticleCategory>(ChosenArticleCategory.class), args.toArray());

	}

	@Override
	public List<ChosenArticleCategory> getList(int pageCount, int pageStart,
			ChosenArticleCategory chosenArticleCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT * FROM CHOSEN_ARTICLE_CATEGORY WHERE LAYER = ? ";

		if (chosenArticleCategory.getLayer() == null) {
			chosenArticleCategory.setLayer("1");
		}

		args.add(chosenArticleCategory.getLayer());

		if (chosenArticleCategory.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(chosenArticleCategory.getParent_id());
		}

		sql += " ORDER BY SORT LIMIT ? OFFSET ? ";

		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<ChosenArticleCategory>(ChosenArticleCategory.class), args.toArray());

	}

	@Override
	public Integer getCount(ChosenArticleCategory chosenArticleCategory) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM CHOSEN_ARTICLE_CATEGORY WHERE LAYER = ? ";

		if (chosenArticleCategory.getLayer() == null) {
			chosenArticleCategory.setLayer("1");
		}

		args.add(chosenArticleCategory.getLayer());

		if (chosenArticleCategory.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(chosenArticleCategory.getParent_id());
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public ChosenArticleCategory getData(ChosenArticleCategory chosenArticleCategory) {

		String sql = " SELECT * FROM CHOSEN_ARTICLE_CATEGORY WHERE ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<ChosenArticleCategory>(ChosenArticleCategory.class),
				new Object[] { chosenArticleCategory.getId() });

	}

	@Override
	public Integer getNextId() {

		Integer nextId = null;
		String sql = "SELECT ID FROM CHOSEN_ARTICLE_CATEGORY ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;

	}

	@Override
	public void add(ChosenArticleCategory chosenArticleCategory) {
		addSort(chosenArticleCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(chosenArticleCategory);

		String sql = " INSERT INTO CHOSEN_ARTICLE_CATEGORY(ID, PARENT_ID, NAME, LAYER, CREATE_BY, CREATE_DATE,"
				+ " UPDATE_BY, UPDATE_DATE, SORT)VALUES(:id, :parent_id, :name, :layer, :create_by, now(), :update_by, now(), :sort) ";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void update(ChosenArticleCategory chosenArticleCategory) {
		sort(chosenArticleCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(chosenArticleCategory);
		String sql = " UPDATE CHOSEN_ARTICLE_CATEGORY SET NAME = :name, UPDATE_BY = :update_by, UPDATE_DATE = now(), SORT = :sort"
				+ " WHERE ID = :id ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void delete(Integer id) {
		deleteSort(id);
		String sql = " DELETE FROM CHOSEN_ARTICLE_CATEGORY WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });
	}

	@Override
	public Integer getCountByName(String categoryName) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) AS COUNT FROM CHOSEN_ARTICLE_CATEGORY WHERE NAME = ?";

		args.add(categoryName);
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public void resetSort(ChosenArticleCategory chosenArticleCategory) {
		if(chosenArticleCategory.getParent_id()==null) {
			chosenArticleCategory.setParent_id(0);
		}
		List<Object> args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM CHOSEN_ARTICLE_CATEGORY WHERE parent_id = ? ";
		args.add(chosenArticleCategory.getParent_id());
		Integer count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		
		String sql2 = "SELECT sort FROM CHOSEN_ARTICLE_CATEGORY WHERE parent_id = ? ORDER BY sort ASC ";
		for(int i=0 ; i<count ; i++) {
			List<NewExamCategory> query = postgresqlJdbcTemplate.query(sql2, new BeanPropertyRowMapper<NewExamCategory>(NewExamCategory.class), args.toArray());
			List<Object> updateArgs = new ArrayList<>();
			String updateSql = "UPDATE CHOSEN_ARTICLE_CATEGORY SET sort = ? WHERE sort = ? AND parent_id = ? ";
			updateArgs.add(i+1);
			updateArgs.add(query.get(i).getSort());
			updateArgs.add(chosenArticleCategory.getParent_id());
			postgresqlJdbcTemplate.update(updateSql, updateArgs.toArray());
		}
	}

	public void deleteSort(Integer id) {
		String sql1 = "SELECT SORT FROM CHOSEN_ARTICLE_CATEGORY WHERE ID = ?";

		ChosenArticleCategory oldArticleCategory = postgresqlJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<ChosenArticleCategory>(ChosenArticleCategory.class),new Object[] {id });

		String sql = "UPDATE CHOSEN_ARTICLE_CATEGORY SET SORT = SORT -1 WHERE SORT > ? AND PARENT_ID = ?";
		
		postgresqlJdbcTemplate.update(sql,new Object[] { oldArticleCategory.getSort(), id });

	}

	public void addSort(ChosenArticleCategory chosenArticleCategory) {
		if(chosenArticleCategory.getParent_id()==null) {
			chosenArticleCategory.setParent_id(0);
		}
		String sql = "UPDATE CHOSEN_ARTICLE_CATEGORY SET SORT = SORT +1 WHERE SORT >= ? AND PARENT_ID = ?";
		postgresqlJdbcTemplate.update(sql,new Object[] { chosenArticleCategory.getSort(), chosenArticleCategory.getParent_id() });
	}

	public void sort(ChosenArticleCategory chosenArticleCategory) {
		String sql = "SELECT COUNT(*) FROM CHOSEN_ARTICLE_CATEGORY WHERE SORT = ? AND PARENT_ID = ?";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, new Object[] {chosenArticleCategory.getSort(), chosenArticleCategory.getParent_id()}); 
				
		// 若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT SORT FROM CHOSEN_ARTICLE_CATEGORY WHERE ID = ? AND PARENT_ID = ?";

			ChosenArticleCategory oldArticleCategory = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<ChosenArticleCategory>(ChosenArticleCategory.class),new Object[] {chosenArticleCategory.getId(), chosenArticleCategory.getParent_id()});
					
			if (chosenArticleCategory.getSort() < oldArticleCategory.getSort()) {

				// 將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE CHOSEN_ARTICLE_CATEGORY SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ?";
				postgresqlJdbcTemplate.update(sql , new Object[] {oldArticleCategory.getSort(),chosenArticleCategory.getSort(), chosenArticleCategory.getParent_id()});
				
			} else if (chosenArticleCategory.getSort() > oldArticleCategory.getSort()) {

				// 將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE CHOSEN_ARTICLE_CATEGORY SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ?";
				postgresqlJdbcTemplate.update(sql , new Object[] {chosenArticleCategory.getSort(),oldArticleCategory.getSort(), chosenArticleCategory.getParent_id()});
			}
		}
		sql = "UPDATE CHOSEN_ARTICLE_CATEGORY SET SORT = ? WHERE ID = ? AND PARENT_ID = ?";
		postgresqlJdbcTemplate.update(sql , new Object[] {chosenArticleCategory.getSort(), chosenArticleCategory.getId(),
				chosenArticleCategory.getParent_id()});
	}

	
//	=========
	
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from chosen_article_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(ChosenArticleCategory chosenArticleCategory) {
		String sql = " INSERT into chosen_article_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id,name = :name,layer = :layer,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(chosenArticleCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
