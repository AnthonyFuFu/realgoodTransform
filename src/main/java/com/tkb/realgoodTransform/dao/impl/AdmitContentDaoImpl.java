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

import com.tkb.realgoodTransform.dao.AdmitContentDao;
import com.tkb.realgoodTransform.model.AdmitContent;


@Repository
public class AdmitContentDaoImpl implements AdmitContentDao {

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
	public Integer getNextContentId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM ADMIT_CONTENT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;

	}

	@Override
	public List<AdmitContent> getFrontContentList(AdmitContent admitContent) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM ADMIT_CONTENT WHERE ADMIT_ID = ? ORDER BY ID ASC";

		args.add(admitContent.getAdmit_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<AdmitContent>(AdmitContent.class),
				args.toArray());
	}

	@Override
	public List<AdmitContent> getContentListByAdmitId(Integer admit_id) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM ADMIT_CONTENT WHERE ADMIT_ID = ? ORDER BY ID ASC";

		args.add(admit_id);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<AdmitContent>(AdmitContent.class),
				args.toArray());

	}

	@Override
	public void delete(Integer id) {
		String sql = " DELETE FROM ADMIT_CONTENT WHERE ADMIT_ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});

	}

	
//	==========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from admit_content";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(AdmitContent admitContent) {
		String sql = " INSERT into admit_content (id,admit_id,title,summary,create_by,create_date) VALUES (:id,:admit_id,:title,:summary,:create_by,:create_date) "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET admit_id = :admit_id,title = :title,summary = :summary,create_by = :create_by,create_date = :create_date ";
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(admitContent);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
