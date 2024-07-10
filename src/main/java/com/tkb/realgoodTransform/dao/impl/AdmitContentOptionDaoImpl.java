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

import com.tkb.realgoodTransform.dao.AdmitContentOptionDao;
import com.tkb.realgoodTransform.model.AdmitContentOption;



@Repository
public class AdmitContentOptionDaoImpl implements AdmitContentOptionDao {

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
	public Integer getNextContentOptionId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM ADMIT_CONTENT_OPTION ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public List<AdmitContentOption> getFrontContentOptionList(AdmitContentOption admitContentOption) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM ADMIT_CONTENT_OPTION WHERE CONTENT_ID = ? ORDER BY ID ASC";

		args.add(admitContentOption.getContent_id());
		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<AdmitContentOption>(AdmitContentOption.class), args.toArray());
	}

	@Override
	public List<AdmitContentOption> getContentOptionListByContentId(Integer content_id) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM ADMIT_CONTENT_OPTION WHERE CONTENT_ID = ? ORDER BY ID ASC";

		args.add(content_id);
		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<AdmitContentOption>(AdmitContentOption.class), args.toArray());
	}

	@Override
	public void delete(Integer id) {

		String sql = " DELETE FROM ADMIT_CONTENT_OPTION WHERE CONTENT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });

	}
	
	
//	==========
	

	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from admit_content_option";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(AdmitContentOption admitContentOption) {
		String sql = " INSERT into admit_content_option (id,content_id,achievement,name,create_by,create_date) VALUES (:id,:content_id,:achievement,:name,:create_by,:create_date) "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET content_id = :content_id,achievement = :achievement,name = :name,create_by = :create_by,create_date = :create_date";
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(admitContentOption);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
