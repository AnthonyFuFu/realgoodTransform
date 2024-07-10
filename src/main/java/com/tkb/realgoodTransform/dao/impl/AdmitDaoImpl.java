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

import com.tkb.realgoodTransform.dao.AdmitDao;
import com.tkb.realgoodTransform.model.Admit;
import com.tkb.realgoodTransform.model.AdmitContent;
import com.tkb.realgoodTransform.model.AdmitContentOption;
import com.tkb.realgoodTransform.model.AdmitDetail;
import com.tkb.realgoodTransform.model.AdmitLog;



/**
 * 金榜Dao實作類
 * 
 * @author Ken
 * @version 創建時間：2016-04-26
 */
@Repository
public class AdmitDaoImpl implements AdmitDao {

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
	public List<Admit> getList(int pageCount, int pageStart, Admit admit) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT AD.*,ADC.NAME AS CATEGORY_NAME FROM ADMIT AD"
				+ " LEFT JOIN ADMIT_CATEGORY ADC ON ADC.ID = AD.ADMIT_CATEGORY ";

		if (admit.getTitle() != null && !"".equals(admit.getTitle())) {
			sql += " WHERE AD.TITLE LIKE ? ";
			args.add("%" + admit.getTitle() + "%");
		}

		sql += " ORDER BY AD.UPDATE_DATE DESC LIMIT ? OFFSET ? ";

		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Admit>(Admit.class), args.toArray());

	}

	@Override
	public List<Admit> getFrontList() {
		String sql = " SELECT AD.*,ADC.NAME AS CATEGORY_NAME FROM ADMIT AD LEFT JOIN ADMIT_CATEGORY ADC ON AD.ADMIT_CATEGORY = ADC.ID WHERE DISPLAY = '1' ORDER BY AD.UPDATE_DATE DESC LIMIT  5";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Admit>(Admit.class));
	}

	@Override
	public List<Admit> getIndexList() {
		String sql = " SELECT AD.*,ADC.NAME AS CATEGORY_NAME FROM ADMIT AD LEFT JOIN ADMIT_CATEGORY ADC ON AD.ADMIT_CATEGORY = ADC.ID WHERE DISPLAY = '1' ORDER BY AD.UPDATE_DATE DESC LIMIT  5";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Admit>(Admit.class));
	}

	@Override
	public List<Admit> getFrontList(int pageCount, int pageStart, Admit admit) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT AD.*,ADC.NAME AS CATEGORY_NAME " + " FROM ADMIT AD "
				+ " LEFT JOIN ADMIT_CATEGORY ADC ON ADC.ID = AD.ADMIT_CATEGORY WHERE DISPLAY = '1' ";

		if (admit.getAdmit_category() != null && admit.getAdmit_category() != 0) {
			sql += " AND AD.ADMIT_CATEGORY = ? ";
			args.add(admit.getAdmit_category());
		}

		if (admit.getAdmit_year() != null && admit.getAdmit_year() != 0 ) {
			sql += " AND AD.ADMIT_YEAR = ? ";
			args.add(admit.getAdmit_year());
		}

		sql += " ORDER BY AD.UPDATE_DATE DESC LIMIT ? OFFSET ?";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Admit>(Admit.class), args.toArray());

	}

	@Override
	public Integer getCount(Admit admit) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM ADMIT ";

		if (admit.getTitle() != null && !"".equals(admit.getTitle())) {
			sql += " WHERE TITLE LIKE ? ";
			args.add("%" + admit.getTitle() + "%");
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public Integer getFrontCount(Admit admit) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM ADMIT AD "
//				   + " LEFT JOIN ADMIT_CATEGORY ADC ON ADC.ID = AD.ADMIT_CATEGORY "
				+ " WHERE DISPLAY = '1' ";

		if (admit.getAdmit_category() != null && admit.getAdmit_category() != 0) {
			sql += " AND AD.ADMIT_CATEGORY = ? ";
			args.add(admit.getAdmit_category());
		}

		if (admit.getAdmit_year() != null && admit.getAdmit_year() != 0) {
			sql += " AND AD.ADMIT_YEAR = ? ";
			args.add(admit.getAdmit_year());
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public Admit getData(Admit admit) {
		String sql = " SELECT * FROM ADMIT WHERE ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Admit>(Admit.class),
				new Object[] { admit.getId() });

	}

	@Override
	public Admit getFrontData(Admit admit) {
		String sql = " SELECT * FROM ADMIT WHERE ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Admit>(Admit.class),
				new Object[] { admit.getId() });

	}

	@Override
	public List<Admit> getAllAdmitYear() {
		String sql = " SELECT DISTINCT ADMIT_YEAR FROM ADMIT ORDER BY ADMIT_YEAR ASC";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Admit>(Admit.class));
	}

	@Override
	public Integer getCountListByCategory(int categoryId) {
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT COUNT(*) AS COUNT FROM ADMIT WHERE ADMIT_CATEGORY = ? ";
		args.add(categoryId);
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
//		String sql = "SELECT ADMIT_ID FROM ADMIT_DETAIL ORDER BY ADMIT_ID DESC LIMIT 1";
		String sql = "SELECT admit.id FROM admit ORDER BY admit.id DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class)+1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(Admit admit) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admit);
		String sql = " INSERT INTO ADMIT(ID, TITLE, BANNER_CONTENT_BLACK, BANNER_CONTENT_RED, TOTAL_LIST,"
				+ " TOTAL_TITLE, TOTAL_CONTENT, FILE_URL, CREATE_BY, CREATE_DATE,"
				+ " UPDATE_BY, UPDATE_DATE, ADMIT_YEAR,ADMIT_CATEGORY,IMAGE_URL,BANNER_URL,DISPLAY,SEO_CONTENT,ENCRYPTURL) "
				+ " VALUES(:id,:title,:banner_content_black,:banner_content_red,:total_list,:total_title, "
				+ " :total_content,:file_url,:create_by, now(),:update_by, now(), :admit_year,:admit_category, "
				+ " :image_url, :banner_url, :display,:seo_content,:encrypturl) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void addContent(AdmitContent admitContent) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admitContent);
		String sql = " INSERT INTO ADMIT_CONTENT(ID, ADMIT_ID, TITLE, SUMMARY,CREATE_BY, CREATE_DATE)"
				+ " VALUES(:id, :admit_id, :title, :summary, :create_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void addContentOption(AdmitContentOption admitContentOption) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admitContentOption);
		String sql = " INSERT INTO ADMIT_CONTENT_OPTION(ID, CONTENT_ID, ACHIEVEMENT, NAME,CREATE_BY, CREATE_DATE)"
				+ " values(:id, :content_id, :achievement, :name, :create_by,now()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void addDetail(AdmitDetail admitDetail) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admitDetail);
		String sql = " INSERT INTO ADMIT_DETAIL(ID, ADMIT_ID, NAME, TYPE, ACHIEVEMENT,"
				+ " CREATE_BY, CREATE_DATE,UPDATE_BY, UPDATE_DATE)"
				+ " VALUES(:id, :admit_id, :name, :type, :achievement, :create_by,now(), :update_by, now()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void update(Admit admit) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(admit);
		String sql = " UPDATE ADMIT SET TITLE = :title, BANNER_CONTENT_BLACK = :banner_content_black, BANNER_CONTENT_RED = :banner_content_red,"
				+ " TOTAL_LIST = :total_list, TOTAL_TITLE = :total_title, TOTAL_CONTENT = :total_content, "
				+ " ADMIT_YEAR = :admit_year, ADMIT_CATEGORY = :admit_category, IMAGE_URL = :image_url, BANNER_URL = :banner_url,"
				+ " DISPLAY = :display,UPDATE_BY = :update_by,UPDATE_DATE = now(),SEO_CONTENT = :seo_content,ENCRYPTURL = :encrypturl "
				+ " WHERE ID = :id";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void updateClickRate(Admit admit) {
		String sql = " UPDATE ADMIT SET CLICK_RATE = CLICK_RATE+1 WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { admit.getId() });
	}

	@Override
	public void delete(Integer id) {
		String sql = " DELETE FROM ADMIT WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });

	}

	@Override
	public void updateDisplayHide(Integer id) {
		String sql = " UPDATE ADMIT SET DISPLAY = '0' WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });
	}

	@Override
	public void updateDisplayShow(Integer id) {
		String sql = " UPDATE ADMIT SET DISPLAY = '1' WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });
	}

	@Override
	public void addAdmitLog(AdmitLog admitLog) {

		String sql = " INSERT INTO ADMIT_LOG(ID, ADMIT_ID, ACTION_TYPE, TITLE, " + " CREATE_BY, CREATE_DATE)"
				+ " VALUES(?, ?, ?, ?, ?, now())";

		postgresqlJdbcTemplate.update(sql, new Object[] { admitLog.getId(), admitLog.getAdmit_id(),
				admitLog.getAction_type(), admitLog.getTitle(), admitLog.getCreate_by() });

	}

	@Override
	public Integer getNextAdmitLogId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM ADMIT_LOG ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public List<Admit> getList(Admit admit) {
		String sql = " SELECT * FROM ADMIT";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Admit>(Admit.class));
	}

	@Override
	public void updateId(Admit admit) {
		String sql = " UPDATE ADMIT SET ENCRYPTURL = ? WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] { admit.getStr(), admit.getId() });

	}

	
//	=============
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from admit";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(Admit admit) {
		String sql = " INSERT into admit (id,title,banner_content_black,banner_content_red,total_list,total_title,total_content,file_url,create_by,create_date,update_by,update_date,click_rate,admit_year,admit_category,image_url,banner_url,display,seo_content,encrypturl) VALUES (:id,:title,:banner_content_black,:banner_content_red,:total_list,:total_title,:total_content,:file_url,:create_by,:create_date,:update_by,CAST(:update_date AS TIMESTAMP),:click_rate,:admit_year,:admit_category,:image_url,:banner_url,:display,:seo_content,:encrypturl )  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET title = :title,banner_content_black = :banner_content_black,banner_content_red = :banner_content_red,total_list = :total_list,total_title = :total_title,total_content = :total_content,file_url = :file_url,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = CAST(:update_date AS TIMESTAMP),click_rate = :click_rate,admit_year = :admit_year,admit_category = :admit_category,image_url = :image_url,banner_url = :banner_url,display = :display,seo_content = :seo_content,encrypturl = :encrypturl ";
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(admit);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}
}
