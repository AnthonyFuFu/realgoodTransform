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

import com.tkb.realgoodTransform.dao.GSATCategoryDao;
import com.tkb.realgoodTransform.model.GSATCategory;
import com.tkb.realgoodTransform.model.GSATMenu;

/**
 * 外特類別Dao實作類
 * @author cheng
 * @version 創建時間：2017-02-08
 */
@Repository
public class GSATCategoryDaoImpl implements GSATCategoryDao{
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
	public List<GSATCategory> getSubList(GSATCategory gSATCategory) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_CATEGORY WHERE PARENT_ID = ? ";
		
		args.add(gSATCategory.getParent_id());
		
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATCategory>(GSATCategory.class),args.toArray());

		
	}
	
	//有再使用的方法add
	@Override
	public List<GSATCategory> getLayerList(String layer, GSATCategory gSATCategory) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_CATEGORY WHERE PARENT_ID = ? AND LAYER = ?  ORDER BY SORT";
		
		args.add(gSATCategory.getParent_id());
		args.add(layer);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATCategory>(GSATCategory.class),args.toArray());
		
	}
	
	/*
	 * 後台清單頁
	 */
	@Override
	public List<GSATCategory> getList(int pageCount, int pageStart, GSATCategory gSATCategory) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_CATEGORY WHERE LAYER = ? ";			
		
		if(gSATCategory.getLayer() == null) {
			gSATCategory.setLayer("1");
		}
		
		args.add(gSATCategory.getLayer());
		
		if(gSATCategory.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(gSATCategory.getParent_id());
		}	
		
		if(gSATCategory.getName() != null && !"".equals(gSATCategory.getName())) {
			sql += " AND NAME LIKE ? ";
			args.add("%"+gSATCategory.getName()+"%");
		}		
		
		sql += " ORDER BY SORT LIMIT ? OFFSET ? ";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATCategory>(GSATCategory.class),args.toArray());

		
	}
	
	/*
	 * 後台資料筆數計算
	 */
	@Override
	public Integer getCount(GSATCategory gSATCategory) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_CATEGORY WHERE LAYER = ? ";		
		
		if(gSATCategory.getLayer() == null) {
			gSATCategory.setLayer("1");
		}
		
		args.add(gSATCategory.getLayer());
		
		if(gSATCategory.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(gSATCategory.getParent_id());
		}			
		
		if(gSATCategory.getName() != null && !"".equals(gSATCategory.getName())) {
			sql += " AND NAME LIKE ? ";
			args.add("%"+gSATCategory.getName()+"%");
		}		
		
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
		
	}
	
	/*
	 * 抓取資料
	 */
	@Override
	public GSATCategory getData(GSATCategory gSATCategory) {
		
		String sql = " SELECT * FROM GSAT_CATEGORY WHERE ID = ? ";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATCategory>(GSATCategory.class),new Object[] {gSATCategory.getId()});

		
	}
	
	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_CATEGORY ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}
	
	/*
	 * 新增資料功能
	 */
	@Override
	public void add(GSATCategory gSATCategory) {
		addSort(gSATCategory);
		String sql = " INSERT INTO GSAT_CATEGORY "
				   + " (ID, PARENT_ID, NAME, LAYER, SORT, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				   + " VALUES(?, ?, ?, ?,?, ?, now(), ?, now()) ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATCategory.getId(), gSATCategory.getParent_id(),
				gSATCategory.getName(), gSATCategory.getLayer(),gSATCategory.getSort(), gSATCategory.getCreate_by(), gSATCategory.getUpdate_by() });
		
	}
	
	/*
	 * 修改資料
	 */
	@Override
	public void update(GSATCategory gSATCategory) {
		sort(gSATCategory);
		String sql = " UPDATE GSAT_CATEGORY SET NAME = ?,SORT = ?, UPDATE_BY = ?, UPDATE_DATE = now()"
				   + " WHERE ID = ? ";
		postgreJdbcTemplate.update(sql, new Object[] { gSATCategory.getName(),gSATCategory.getSort(),
				gSATCategory.getUpdate_by(), gSATCategory.getId() });
		
	}
	
	public void addSort(GSATCategory gSATCategory){
		String sql = "UPDATE GSAT_CATEGORY SET SORT = SORT +1 WHERE SORT >= ? AND PARENT_ID = ?";
		postgreJdbcTemplate.update(sql, new Object[]{ gSATCategory.getSort(), gSATCategory.getParent_id() });
	}
	
	public void sort(GSATCategory gSATCategory) {
		String sql = "SELECT COUNT(*) FROM GSAT_CATEGORY WHERE SORT = ? AND PARENT_ID = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSATCategory.getSort(), gSATCategory.getParent_id()});
		//若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT SORT FROM GSAT_CATEGORY WHERE ID = ? AND PARENT_ID = ?";
			
			GSATMenu oldGSATCategory = postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),new Object[] { gSATCategory.getId(), gSATCategory.getParent_id()});

			if (gSATCategory.getSort() < oldGSATCategory.getSort()) {
				
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE GSAT_CATEGORY SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ?";
				postgreJdbcTemplate.update(sql, new Object[]{ oldGSATCategory.getSort(), gSATCategory.getSort(), gSATCategory.getParent_id() });
			} else if (gSATCategory.getSort() > oldGSATCategory.getSort()){
				
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE GSAT_CATEGORY SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ?";
				postgreJdbcTemplate.update(sql, new Object[]{ gSATCategory.getSort(), oldGSATCategory.getSort(), gSATCategory.getParent_id()});
			}			
		}
		sql = "UPDATE GSAT_CATEGORY SET SORT = ? WHERE ID = ? AND PARENT_ID = ?";
		postgreJdbcTemplate.update(sql, new Object[]{gSATCategory.getSort(), gSATCategory.getId(), gSATCategory.getParent_id()});
	}
	
	public void updateEditor(GSATCategory gSATCategory) {
		String sql = "UPDATE GSAT_CATEGORY SET UPDATE_BY = ?, UPDATE_DATE = now()";
		postgreJdbcTemplate.update(sql, new Object[] { gSATCategory.getUpdate_by() });
	}
	
	@Override
	public void resetSort(GSATCategory gSATCategory){
		gSATCategory.setParent_id(0);
		String sql = "SELECT COUNT(*) FROM GSAT_CATEGORY WHERE PARENT_ID = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSATCategory.getParent_id()});
		int parent_id = gSATCategory.getParent_id();
		sql = "SELECT SORT FROM GSAT_CATEGORY WHERE PARENT_ID = ? ORDER BY SORT ASC";
		List<Integer>sortList = postgreJdbcTemplate.queryForList(sql,Integer.class, new Object[] { parent_id });
		for(int i=0; i<count; i++){
			String sql2 = "UPDATE GSAT_CATEGORY SET SORT = ? WHERE SORT = ? AND PARENT_ID = "+parent_id;
			postgreJdbcTemplate.update(sql2, new Object[] {i+1, sortList.get(i)});
		}
	}
	
	public void deleteSort(GSATCategory gSATCategory){
		gSATCategory.setParent_id(0);
		String sql1 = "SELECT SORT FROM GSAT_CATEGORY WHERE ID = ?";
		GSATCategory oldGSATCategory =  postgreJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<GSATCategory>(GSATCategory.class),new Object[] { gSATCategory.getId()});
		
		String sql = "UPDATE GSAT_CATEGORY SET SORT = SORT -1 WHERE SORT > ? AND PARENT_ID = ?";

		postgreJdbcTemplate.update(sql, new Object[]{ oldGSATCategory.getSort(), gSATCategory.getParent_id() });
	}
	
	/*
	 * 刪除資料
	 */
	@Override
	public void delete(GSATCategory gSATCategory ,Integer id) {		
		deleteSort(gSATCategory);
		String sql = " DELETE FROM GSAT_CATEGORY WHERE ID = ? ";
		postgreJdbcTemplate.update(sql, new Object[] { id });		
	}
	
	@Override
	public String checkGSATCategory(GSATCategory gSATCategory) {
		String sql = "SELECT * FROM GSAT_CATEGORY WHERE NAME = ? AND LAYER = ?";
		List<GSATCategory> list = postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATCategory>(GSATCategory.class),new Object[]{gSATCategory.getName(),gSATCategory.getLayer()});
		return list.size() == 0 ? null : list.get(0).getName();
	}	
	
	@Override
	public Integer getCategoryId(String name) {
		String sql = " SELECT ID FROM GSAT_CATEGORY WHERE NAME = ? ";
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{name});
	}	
	
	
	
//	===========================
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from GSAT_CATEGORY";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}
	@Override
	public void updateNormalData(GSATCategory gSATCategory) {
		String sql = " INSERT into gsat_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
					+ " ON CONFLICT(id) "
					+ " DO UPDATE SET parent_id = :parent_id, name = :name, layer = :layer, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, sort = :sort "; 
		
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(gSATCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}
	
}
