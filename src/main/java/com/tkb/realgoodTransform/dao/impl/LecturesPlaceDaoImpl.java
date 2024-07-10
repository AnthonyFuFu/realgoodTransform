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

import com.tkb.realgoodTransform.dao.LecturesPlaceDao;
import com.tkb.realgoodTransform.model.LecturesPlace;



@Repository
public class LecturesPlaceDaoImpl implements LecturesPlaceDao {

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
	public List<LecturesPlace> getList(LecturesPlace lecturesPlace) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_PLACE WHERE LECTURES_ID = ? ORDER BY ID" ;
		args.add(lecturesPlace.getLectures_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesPlace>(LecturesPlace.class),args.toArray());
	}


	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM LECTURES_PLACE ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			e.printStackTrace();
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(LecturesPlace lecturesPlace) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lecturesPlace);
		String sql = "INSERT INTO LECTURES_PLACE(ID, LECTURES_ID, PLACENAME, PLACEEVENT, PLACEDAY, PLACE_ADDRESS, CREATE_BY, "
				   + "CREATE_DATE, UPDATE_BY, UPDATE_DATE) VALUES (:id, :lectures_id, :placeName , :placeEvent, CAST(:placeDay AS TIMESTAMP), :place_address, :create_by,"
				   + "now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(LecturesPlace lecturesPlace) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(lecturesPlace);
		String sql = "UPDATE LECTURES_PLACE SET PLACEEVENT = :placeEvent, PLACENAME = :placeName , PLACEDAY = CAST(:placeDay AS TIMESTAMP), UPDATE_BY = :update_by, "
				   + "UPDATE_DATE = :update_date WHERE ID = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM LECTURES_PLACE WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}

	@Override
	public List<LecturesPlace> getPlaceList(LecturesPlace lecturesPlace) {
		List<Object>args = new ArrayList<>();
		String sql = " SELECT DISTINCT(LECTURES_ID),PLACENAME FROM LECTURES_PLACE "
				   + " WHERE LECTURES_ID = ? ";
		
		args.add(lecturesPlace.getLectures_id()); 
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesPlace>(LecturesPlace.class),args.toArray());
	}
	
	@Override
	public List<LecturesPlace> getAllLecturesPlaces() {
		String sql = "SELECT DISTINCT(NAME) AS PLACENAME,MAPURL AS PLACE_ADDRESS,ID AS AREA_ID FROM AREA "
				   + "WHERE NAME NOT LIKE '%區%' ORDER BY AREA_ID ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesPlace>(LecturesPlace.class));
	}

	@Override
	public List<LecturesPlace> getEventList(LecturesPlace lecturesPlace) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM LECTURES_PLACE WHERE PLACENAME = ? AND LECTURES_ID = ? ORDER BY PLACEDAY ";
		args.add(lecturesPlace.getPlaceName());
		args.add(lecturesPlace.getLectures_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesPlace>(LecturesPlace.class),args.toArray());
	}
	
	@Override
	public List<LecturesPlace> getAllEventList(LecturesPlace lecturesPlace) {
		String sql = "SELECT * FROM LECTURES_PLACE WHERE LECTURES_ID = ? ORDER BY PLACEDAY ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<LecturesPlace>(LecturesPlace.class),lecturesPlace.getLectures_id());
	}


	@Override
	public List<LecturesPlace> getList(int pageCount, int pageStart, LecturesPlace lecturesPlace) {
		// TODO Auto-generated method stub
		return null;
	}


//	==============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from lectures_place";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}


	@Override
	public void updateNormalData(LecturesPlace lecturesPlace) {
		String sql = " INSERT into lectures_place (id,lectures_id,placename,placeday,create_by,create_date,update_by,update_date,placeevent,place_address) VALUES (:id,:lectures_id,:placeName,CAST(:placeDay AS TIMESTAMP),:create_by,:create_date,:update_by,:update_date,:placeEvent,:place_address)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET lectures_id = :lectures_id,placeName = :placeName,placeDay = CAST(:placeDay AS TIMESTAMP),create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,placeEvent = :placeEvent,place_address = :place_address "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lecturesPlace);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
