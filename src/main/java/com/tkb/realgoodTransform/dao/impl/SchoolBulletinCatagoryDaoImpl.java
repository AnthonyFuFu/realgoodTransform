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

import com.tkb.realgoodTransform.dao.SchoolBulletinCategoryDao;
import com.tkb.realgoodTransform.model.SchoolBulletinCategory;





@Repository
public class SchoolBulletinCatagoryDaoImpl implements SchoolBulletinCategoryDao {
	
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
	public List<SchoolBulletinCategory> getSubList(SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN_CATEGORY WHERE PARENT_ID = ? ";
		args.add(schoolBulletinCategory.getParent_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class), args.toArray());
	}

	@Override
	public List<SchoolBulletinCategory> getLayerList(String layer, SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN_CATEGORY WHERE PARENT_ID = ? AND LAYER = ? ORDER BY SORT ";
		args.add(schoolBulletinCategory.getParent_id());
		args.add(layer);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class), args.toArray());
	}

	@Override
	public List<SchoolBulletinCategory> getList(int pageCount, int pageStart,
			SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN_CATEGORY WHERE LAYER = ? ";
		if(schoolBulletinCategory.getLayer() == null) {
			schoolBulletinCategory.setLayer("1");
		}
		args.add(schoolBulletinCategory.getLayer());
		if(schoolBulletinCategory.getParent_id() != null) {
			sql += "AND PARENT_ID = ? ";
			args.add(schoolBulletinCategory.getParent_id());
		}
		sql += " ORDER BY SORT LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class), args.toArray());
	}

	@Override
	public Integer getCount(SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN_CATEGORY WHERE LAYER = ? ";
		if(schoolBulletinCategory.getLayer() == null) {
			schoolBulletinCategory.setLayer("1");
		}
		args.add(schoolBulletinCategory.getLayer());
		if(schoolBulletinCategory.getParent_id() != null) {
			sql += "AND PARENT_ID = ?";
			args.add(schoolBulletinCategory.getParent_id());
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public SchoolBulletinCategory getData(SchoolBulletinCategory schoolBulletinCategory) {
		String sql ="SELECT * FROM SCHOOL_BULLETIN_CATEGORY WHERE ID = ? ";
		
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class), new Object[] {schoolBulletinCategory.getId()});
	}

	@Override
	public SchoolBulletinCategory checkRepeat(SchoolBulletinCategory schoolBulletinCategory) {
		String sql = "SELECT * FROM SCHOOL_BULLETIN_CATEGORY WHERE NAME = ? ";
		List<SchoolBulletinCategory> list = postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class),new Object[] {schoolBulletinCategory.getName()});
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM SCHOOL_BULLETIN_CATEGORY ORDER BY ID  DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		}catch(Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(SchoolBulletinCategory schoolBulletinCategory) {
		addSort(schoolBulletinCategory);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(schoolBulletinCategory);
		String sql = "INSERT INTO SCHOOL_BULLETIN_CATEGORY(ID, PARENT_ID, NAME, LAYER, SORT, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)"
				   + "VALUES (:id, :parent_id, :name, :layer, :sort, :create_by, now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void update(SchoolBulletinCategory schoolBulletinCategory) {
		sort(schoolBulletinCategory);
		List<Object> args = new ArrayList<Object>();
		String sql = "UPDATE SCHOOL_BULLETIN_CATEGORY SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ";
		args.add(schoolBulletinCategory.getUpdate_by());
		if(schoolBulletinCategory.getName() != null) {
			sql += "NAME = ?, ";
			args.add(schoolBulletinCategory.getName());
		}
		if(schoolBulletinCategory.getSort() != null) {
			sql += "SORT = ? ";
			args.add(schoolBulletinCategory.getSort());
		}
		sql += "WHERE ID = ?";
		args.add(schoolBulletinCategory.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}
	
	public void sort(SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN_CATEGORY WHERE SORT = ? AND PARENT_ID = ? ";
		args.add(schoolBulletinCategory.getSort());
		args.add(schoolBulletinCategory.getParent_id());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		//若原有排序號碼已存在
		if(count>0) {
			sql = "SELECT SORT FROM SCHOOL_BULLETIN_CATEGORY WHERE ID = ? AND PARENT_ID = ? ";
			SchoolBulletinCategory oldSchoolBulletinCategory = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class), new Object[] {schoolBulletinCategory.getId(), schoolBulletinCategory.getParent_id()});
			if(schoolBulletinCategory.getSort()<oldSchoolBulletinCategory.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE SCHOOL_BULLETIN_CATEGORY SET SORT = SORT + 1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {oldSchoolBulletinCategory.getSort(), schoolBulletinCategory.getSort(), schoolBulletinCategory.getParent_id()});
			}else if(schoolBulletinCategory.getSort()>oldSchoolBulletinCategory.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE SCHOOL_BULLETIN_CATEGORY SET SORT = SORT - 1 WHERE  ? >= SORT AND SORT >= ? AND PARENT_ID = ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {schoolBulletinCategory.getSort(), oldSchoolBulletinCategory.getSort(), schoolBulletinCategory.getParent_id()});
			}
		}
		sql = "UPDATE SCHOOL_BULLETIN_CATEGORY SET SORT = ? WHERE ID = ? AND PARENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {schoolBulletinCategory.getSort(), schoolBulletinCategory.getId(), schoolBulletinCategory.getParent_id()});
	}
	
	public void addSort(SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<>();
		String sql = "UPDATE SCHOOL_BULLETIN_CATEGORY SET SORT = SORT + 1 WHERE SORT >= ? AND PARENT_ID = ? ";
		args.add(schoolBulletinCategory.getSort());
		args.add(schoolBulletinCategory.getParent_id());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void delete(SchoolBulletinCategory schoolBulletinCategory, Integer id) {
		deleteSort(schoolBulletinCategory);
		String sql = "DELETE FROM SCHOOL_BULLETIN_CATEGORY WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql,new Object[] {id});

	}
	
	public void deleteSort(SchoolBulletinCategory schoolBulletinCategory) {
		String sql1 = "SELECT SORT FROM SCHOOL_BULLETIN_CATEGORY WHERE ID = ? ";
		SchoolBulletinCategory oldSchoolBulletinCategory = postgresqlJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<SchoolBulletinCategory>(SchoolBulletinCategory.class), new Object[] {schoolBulletinCategory.getId()});
		String sql = "UPDATE SCHOOL_BULLETIN_CATEGORY SET SORT = SORT - 1 WHERE SORT > ? AND PARENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {oldSchoolBulletinCategory.getSort(), schoolBulletinCategory.getParent_id()});
	}

	@Override
	public void resetSort(SchoolBulletinCategory schoolBulletinCategory) {
		List<Object> args = new ArrayList<>();
		List<Object> args2 = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN_CATEGORY WHERE PARENT_ID = ? ";
		args.add(schoolBulletinCategory.getParent_id());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		sql = "SELECT SORT FROM SCHOOL_BULLETIN_CATEGORY WHERE PARENT_ID = ? ORDER BY SORT ASC ";
		args2.add(schoolBulletinCategory.getParent_id());
		List<Integer>sortList = postgresqlJdbcTemplate.queryForList(sql,Integer.class,args2.toArray());
		for(int i = 0;i<count;i++) {
			String sql2 = "UPDATE SCHOOL_BULLETIN_CATEGORY SET SORT = ? WHERE SORT = ? AND PARENT_ID = ? ";
			postgresqlJdbcTemplate.update(sql2, new Object[] {i+1, sortList.get(i), schoolBulletinCategory.getParent_id() });
		}

	}

	@Override
	public Integer deleteCheck(SchoolBulletinCategory schoolBulletinCategory) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN WHERE CAST(CATEGORY AS INTEGER) = ? ";
		args.add(schoolBulletinCategory.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	
//	================
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from school_bulletin_category";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(SchoolBulletinCategory schoolBulletinCategory) {
		String sql = " INSERT into school_bulletin_category (id,parent_id,name,layer,create_by,create_date,update_by,update_date,sort) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id, name = :name, layer = :layer, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(schoolBulletinCategory);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);		
	}

}
