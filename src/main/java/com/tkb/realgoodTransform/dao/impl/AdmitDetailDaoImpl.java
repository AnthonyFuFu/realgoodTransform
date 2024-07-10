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

import com.tkb.realgoodTransform.dao.AdmitDetailDao;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitDetailLog;



@Repository
public class AdmitDetailDaoImpl implements AdmitDetailDao {

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
	public Integer getNextDetailId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM ADMIT_DETAIL ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public Integer getNextDetailLogId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM ADMIT_DETAIL_LOG ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public AdmitDetail getData(AdmitDetail admitDetail) {

		String sql = " SELECT * FROM ADMIT_DETAIL WHERE ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<AdmitDetail>(AdmitDetail.class),
				new Object[] { admitDetail.getId() });

	}

	@Override
	public List<AdmitDetail> getDetailList(AdmitDetail admitDetail) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM ADMIT_DETAIL WHERE ADMIT_ID = ? ORDER BY ID ASC ";

		args.add(admitDetail.getAdmit_id());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<AdmitDetail>(AdmitDetail.class),
				args.toArray());

	}

	@Override
	public void updateDetail(int pId, String pName, String pType, String pAchievement, String pUser) {

		String sql = " UPDATE ADMIT_DETAIL SET NAME = ?, TYPE = ?, ACHIEVEMENT = ?,UPDATE_BY = ?,UPDATE_DATE = now() "
				+ " WHERE ID = ? ";

		postgresqlJdbcTemplate.update(sql, new Object[] { pName, pType, pAchievement, pUser, pId });
	}

	@Override
	public void deleteDetail(int pId) {

		String sql = " DELETE FROM ADMIT_DETAIL WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { pId });

	}

	@Override
	public void addDetailLog(AdmitDetailLog admitDetailLog) {

		String sql = " INSERT INTO ADMIT_DETAIL_LOG(ID, ADMIT_ID, ADMIT_DETAIL_ID, ACTION_TYPE,PRE_DETAIL,NEW_DETAIL , CREATE_BY, CREATE_DATE)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, now()) ";

		postgresqlJdbcTemplate.update(sql, new Object[] { admitDetailLog.getId(), admitDetailLog.getAdmit_id(),
				admitDetailLog.getAdmit_detail_id(), admitDetailLog.getAction_type(), admitDetailLog.getPre_detail(),
				admitDetailLog.getNew_detail(), admitDetailLog.getCreate_by() });
	}

	
//	=========
	
	
	
	@Override
	public List<Map<String, Object>> getNormalDetailList() {
		String sql = "select * from admit_detail";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalDetailData(AdmitDetail admitDetail) {
		String sql = " INSERT into admit_detail (id,admit_id,name,type,achievement,create_by,create_date,update_by,update_date) VALUES (:id,:admit_id,:name,:type,:achievement,:create_by,:create_date,:update_by,:update_date )  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET admit_id = :admit_id,name = :name,type = :type,achievement = :achievement,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date";
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(admitDetail);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

	@Override
	public List<Map<String, Object>> getNormalDetailLogList() {
		String sql = "select * from admit_detail_log";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalDetailLogData(AdmitDetailLog admitDetailLog) {
		String sql = " INSERT into admit_detail_log (id,admit_id,admit_detail_id,action_type,pre_detail,new_detail,create_by,create_date) VALUES (:id,:admit_id,:admit_detail_id,:action_type,:pre_detail,:new_detail,:create_by,:create_date)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET admit_id = :admit_id,admit_detail_id = :admit_detail_id,action_type = :action_type,pre_detail = :pre_detail,new_detail = :new_detail,create_by = :create_by,create_date = :create_date";
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(admitDetailLog);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

}
