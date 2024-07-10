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

import com.tkb.realgoodTransform.dao.WinnerContentDao;
import com.tkb.realgoodTransform.model.WinnerContent;



@Repository
public class WinnerContentDaoImpl implements WinnerContentDao {

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
	public List<WinnerContent> getList(WinnerContent winnerContent) {
		List<Object> args = new ArrayList<>();
		StringBuffer sql = new StringBuffer("SELECT * FROM WINNER_CONTENT WHERE WINNER_ID = ? ");
		args.add(winnerContent.getWinner_id());
		sql.append("ORDER BY ID ");
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<WinnerContent>(WinnerContent.class),args.toArray());
	}

	@Override
	public List<WinnerContent> getList(int pageCount, int pageStart, WinnerContent winnerContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM WINNER_CONTENT ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(WinnerContent winnerContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(winnerContent);
		String sql = "INSERT INTO WINNER_CONTENT (ID, WINNER_ID, ICON, TITLE, CONTENT, CREATE_BY, "
				   + "CREATE_DATE, UPDATE_BY, UPDATE_DATE ) VALUES(:id, :winner_id, :icon, :title, :content, "
				   + ":create_by, now(), :update_by, now()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(WinnerContent winnerContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(winnerContent);
		String sql = "UPDATE WINNER_CONTENT SET ICON = :icon, TITLE = :title, CONTENT = :content, "
				   + "UPDATE_BY = :update_by, UPDATE_DATE = now() WHERE ID = :id";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM WINNER_CONTENT WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});

	}

	
//	===========
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from winner_content";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(WinnerContent winnerContent) {
		String sql = " INSERT into winner_content (id,winner_id,icon,title,content,create_by,create_date,update_by,update_date) VALUES (:id,:winner_id,:icon,:title,:content,:create_by,:create_date,:update_by,:update_date)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET winner_id = :winner_id,icon = :icon,title = :title,content = :content,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(winnerContent);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
