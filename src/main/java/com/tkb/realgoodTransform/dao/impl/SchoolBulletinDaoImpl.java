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

import com.tkb.realgoodTransform.dao.SchoolBulletinDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.User;



@Repository
public class SchoolBulletinDaoImpl implements SchoolBulletinDao {
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
	public List<SchoolBulletin> getListByCategory(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN WHERE CATEGORY = ? ";
		args.add(schoolBulletin.getCategory());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}

	@Override
	public List<SchoolBulletin> getList(int pageCount, int pageStart, SchoolBulletin schoolBulletin, int groupId) {
		List<Object> args = new ArrayList<Object>();
		
		String sql = "SELECT SB.*, SBC.NAME AS CATEGORY_NAME, SA.NAME AS AREA_NAME "
				   + "FROM SCHOOL_BULLETIN SB "
				   + "LEFT JOIN SCHOOL_BULLETIN_CATEGORY SBC ON SBC.ID = CAST(SB.CATEGORY AS INTEGER) "
				   + "LEFT JOIN AREA SA ON SA.ID = CAST(SB.AREA AS INTEGER) ";
		
		if (schoolBulletin.getArea_id() != 0  && !"admin".equals(schoolBulletin.getAccount())){
			sql += " LEFT JOIN USER_ACCOUNT UA ON UA.ACCOUNT = UPPER(?) " + " WHERE SB.AREA = UA.AREA ";
			args.add(schoolBulletin.getAccount());

			if (schoolBulletin.getTitle() != null && !"".equals(schoolBulletin.getTitle())) {
				sql += " AND SB.TITLE LIKE ? ";
				args.add("%" + schoolBulletin.getTitle() + "%");
			}

		} else {
			if (schoolBulletin.getTitle() != null && !"".equals(schoolBulletin.getTitle())) {
				sql += " WHERE SB.TITLE LIKE ? ";
				args.add("%" + schoolBulletin.getTitle() + "%");
			}
		}

		sql += "ORDER BY SB.BEGIN_DATE DESC , SB.ID DESC LIMIT ? OFFSET ?";

		args.add(pageCount);
		args.add(pageStart);
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}
	
	@Override
	public List<SchoolBulletin> getIndexList(SchoolBulletin schoolBulletin) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT * FROM SCHOOL_BULLETIN WHERE AREA = ? AND CATEGORY = ? "
				+ " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE ";
		args.add(schoolBulletin.getArea());
		args.add(schoolBulletin.getCategory());
		sql += " ORDER BY CREATE_DATE DESC ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());

	}
	
	@Override
	public List<SchoolBulletin> getFrontList(SchoolBulletin schoolBulletin) {
		
		String sql = " SELECT SB.*, SBC.NAME AS CATEGORY_NAME FROM SCHOOL_BULLETIN SB "
				   + " LEFT JOIN SCHOOL_BULLETIN_CATEGORY SBC ON SBC.ID = CAST(SB.CATEGORY AS INTEGER) "
				   + " WHERE TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE "
				   + " ORDER BY END_DATE DESC LIMIT 5";
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class));
	}

	@Override
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort) {
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT SB.*,  "
				   + " SBC.NAME AS CATEGORY_NAME FROM SCHOOL_BULLETIN SB "
				   + " LEFT JOIN SCHOOL_BULLETIN_CATEGORY SBC ON SBC.ID = CAST(SB.CATEGORY AS INTEGER) "
				   + " WHERE 1 = 1 "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		
		if (schoolBulletin.getArea() != null && !"".equals(schoolBulletin.getArea())) {
			sql += "AND SB.AREA = ? ";
			args.add(schoolBulletin.getArea());
		}
		if (schoolBulletin.getCategory() != null && !"".equals(schoolBulletin.getCategory())) {
			sql += "AND SB.CATEGORY = ? ";
			args.add(schoolBulletin.getCategory());
		}
		if ("week".equals(schoolBulletin.getDate())) {

			sql += "AND( "
				+ "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				+ "OR "
				+ "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				+ "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				+ ") ";
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_end_date());
			args.add(schoolBulletin.getWeek_begin_date());

		} else if ("month".equals(schoolBulletin.getDate())) {
			sql += "AND "
				+ "( "
				+ "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ "OR "
				+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ ") ";
		}
		sql += " ORDER BY SB.BEGIN_DATE DESC , SB.id DESC ";
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}

	@Override
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Area> areaList, String search_sort) {
		List<Object> args = new ArrayList<>();

		String sql = " SELECT SB.*,  "
				   + " SBC.NAME AS CATEGORY_NAME FROM SCHOOL_BULLETIN SB "
				   + " LEFT JOIN SCHOOL_BULLETIN_CATEGORY SBC ON SBC.ID = CAST(SB.CATEGORY AS INTEGER) "
				   + " WHERE 1 = 1 "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";

		if (areaList != null && areaList.size() > 0) {
			sql += " AND (";
			for (int i = 0; i < areaList.size(); i++) {
				sql += " CAST(SB.AREA AS INTEGER) = ?   ";
				args.add(areaList.get(i).getId());
				if (i < areaList.size() - 1) {
					sql += " OR ";
				}
			}
			sql += " ) ";
		}
		if (schoolBulletin.getCategory() != null && !"".equals(schoolBulletin.getCategory())) {
			sql += "AND SB.CATEGORY = ? ";
			args.add(schoolBulletin.getCategory());
		}
		if ("week".equals(schoolBulletin.getDate())) {

			sql += " AND( "
				+ " (BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				+ " AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				+ " OR "
				+ " (BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				+ " AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				+ " AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				+ " ) ";
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_end_date());
			args.add(schoolBulletin.getWeek_begin_date());

		} else if ("month".equals(schoolBulletin.getDate())) {
			sql += " AND "
				+ " ( "
				+ " DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ " OR "
				+ " DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ " ) ";
		}
		sql += " ORDER BY SB.BEGIN_DATE DESC , SB.id DESC ";
		sql += " LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}

	@Override
	public Integer getCount(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<Object>();

		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN SB";

		if (schoolBulletin.getArea_id() != 0  && !"admin".equals(schoolBulletin.getAccount())) {
			sql += " LEFT JOIN USER_ACCOUNT UA ON UA.ACCOUNT = UPPER(?) " + " WHERE SB.AREA = UA.AREA ";
			args.add(schoolBulletin.getAccount());

			if (schoolBulletin.getTitle() != null && !"".equals(schoolBulletin.getTitle())) {
				sql += " AND TITLE LIKE ? ";
				args.add("%" + schoolBulletin.getTitle() + "%");
			}
		} else {
			if (schoolBulletin.getTitle() != null && !"".equals(schoolBulletin.getTitle())) {
				sql += " WHERE TITLE LIKE ? ";
				args.add("%" + schoolBulletin.getTitle() + "%");
			}
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public Integer getFrontCount(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN SB "
				   + "WHERE 1=1 "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";
		if (schoolBulletin.getArea() != null && !"".equals(schoolBulletin.getArea())) {
			sql += "AND SB.AREA = ? ";
			args.add(schoolBulletin.getArea());
		}
		if (schoolBulletin.getCategory() != null && !"".equals(schoolBulletin.getCategory())) {
			sql += "AND SB.CATEGORY = ? ";
			args.add(schoolBulletin.getCategory());
		}
		if ("week".equals(schoolBulletin.getDate())) {

			sql += "AND( "
				+ "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				+ "OR "
				+ "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				+ "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				+ ") ";
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_end_date());
			args.add(schoolBulletin.getWeek_begin_date());

		} else if ("month".equals(schoolBulletin.getDate())) {
			sql += "AND "
				+ "( "
				+ "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ "OR "
				+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ ") ";
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public SchoolBulletin getData(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN WHERE ID = ?";
		args.add(schoolBulletin.getId());
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class), args.toArray());
	}

	@Override
	public SchoolBulletin getFrontData(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN WHERE ID = ? ";
		args.add(schoolBulletin.getId());
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM SCHOOL_BULLETIN ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(SchoolBulletin schoolBulletin) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(schoolBulletin);
		String sql = "INSERT INTO SCHOOL_BULLETIN (ID, TITLE, AREA, CATEGORY, IMAGE, BEGIN_DATE, END_DATE, CLICK_RATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, CONTENT, ENCRYPTURL, PRODUCT_CATEGORY) "
				+ "VALUES(:id, :title, :area, :category, :image, :begin_date, :end_date, 0, :create_by, now(), :update_by, now(), :content, :encrypturl, :product_category)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	@Override
	public void update(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<Object>();
		String sql = "UPDATE SCHOOL_BULLETIN SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ";
		args.add(schoolBulletin.getUpdate_by());
		if (schoolBulletin.getTitle() != null) {
			sql += "TITLE = ?, ";
			args.add(schoolBulletin.getTitle());
		}
		if (schoolBulletin.getArea() != null) {
			sql += "AREA = ?, ";
			args.add(schoolBulletin.getArea());
		}
		if (schoolBulletin.getCategory() != null) {
			sql += "CATEGORY = ?, ";
			args.add(schoolBulletin.getCategory());
		}
		if (schoolBulletin.getImage() != null) {
			sql += "IMAGE = ?, ";
			args.add(schoolBulletin.getImage());
		}
		if (schoolBulletin.getBegin_date() != null) {
			sql += "BEGIN_DATE = ?, ";
			args.add(schoolBulletin.getBegin_date());
		}
		if (schoolBulletin.getEnd_date() != null) {
			sql += "END_DATE = ?, ";
			args.add(schoolBulletin.getEnd_date());
		}
		if (schoolBulletin.getProduct_category() != null) {
			sql += "PRODUCT_CATEGORY = ? ";
			args.add(schoolBulletin.getProduct_category());
		}
		sql += "WHERE ID = ?";
		args.add(schoolBulletin.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());

	}

	@Override
	public void updateClickRate(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<>();
		String sql = "UPDATE SCHOOL_BULLETIN SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(schoolBulletin.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM SCHOOL_BULLETIN WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });

	}

	@Override
	public Integer userAccount(User user) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT GROUP_ID FROM GROUP_USER WHERE USER_ID = (SELECT ID FROM USER_ACCOUNT WHERE ACCOUNT = UPPER(?) ) ";
		args.add(user.getAccount());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public List<SchoolBulletin> getRandomList(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM SCHOOL_BULLETIN WHERE ID != ? ORDER BY RANDOM() LIMIT 3";
		args.add(schoolBulletin.getId());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}

	@Override
	public List<SchoolBulletin> getPrevList(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<>();
		String sql = " SELECT * FROM SCHOOL_BULLETIN WHERE  ID < ? AND AREA = ? AND CATEGORY = ? "
				+ " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyy-MM-dd') >= BEGIN_DATE "
				+ " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE ";
		args.add(schoolBulletin.getId());
		args.add(schoolBulletin.getArea());
		args.add(schoolBulletin.getCategory());
		sql += " ORDER BY ID DESC LIMIT 1 ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}

	@Override
	public List<SchoolBulletin> getNextList(SchoolBulletin schoolBulletin) {
		List<Object> args = new ArrayList<>();
		String sql = " SELECT * FROM SCHOOL_BULLETIN WHERE  ID > ? AND AREA = ? AND CATEGORY = ? "
				+ " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE ";
		args.add(schoolBulletin.getId());
		args.add(schoolBulletin.getArea());
		args.add(schoolBulletin.getCategory());
		sql += " ORDER BY ID DESC LIMIT 1 ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class),
				args.toArray());
	}

	@Override
	public Integer getCountByApiLocation(SchoolBulletin schoolBulletin, List<Location> locationList) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM SCHOOL_BULLETIN SB "
				   + "WHERE 1=1 "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";

		if (schoolBulletin.getCategory() != null && !"".equals(schoolBulletin.getCategory())) {
			sql += "AND SB.CATEGORY = ? ";
			args.add(schoolBulletin.getCategory());
		}
		if (locationList != null && locationList.size() > 0) {
			sql += " AND (";
			for (int i = 0; i < locationList.size(); i++) {
				sql += " CAST(SB.AREA AS INTEGER) = ?  ";
				args.add(locationList.get(i).getId());
				if (i < locationList.size() - 1) {
					sql += " OR ";
				}
			}
			sql += " ) ";
		}
		if ("week".equals(schoolBulletin.getDate())) {

			sql += "AND( "
				+ "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				+ "OR "
				+ "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				+ "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				+ ") ";
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_end_date());
			args.add(schoolBulletin.getWeek_begin_date());

		} else if ("month".equals(schoolBulletin.getDate())) {
			sql += "AND "
				+ "( "
				+ "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ "OR "
				+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ ") ";
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public List<SchoolBulletin> getListByApiLocation(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Location> locationList, String search_sort) {
		List<Object> args = new ArrayList<>();

		String sql = " SELECT SB.*,  "
				   + " SBC.NAME AS CATEGORY_NAME FROM SCHOOL_BULLETIN SB "
				   + " LEFT JOIN SCHOOL_BULLETIN_CATEGORY SBC ON SBC.ID = CAST(SB.CATEGORY AS INTEGER) "
				   + " WHERE 1 = 1 "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ";

		if (locationList != null && locationList.size() > 0) {
			sql += " AND (";
			for (int i = 0; i < locationList.size(); i++) {
				sql += " CAST(SB.AREA AS INTEGER) = ?   ";
				args.add(locationList.get(i).getId());
				if (i < locationList.size() - 1) {
					sql += " OR ";
				}
			}
			sql += " ) ";
		}
		if (schoolBulletin.getCategory() != null && !"".equals(schoolBulletin.getCategory())) {
			sql += "AND SB.CATEGORY = ? ";
			args.add(schoolBulletin.getCategory());
		}
		if ("week".equals(schoolBulletin.getDate())) {

			sql += "AND( "
				+ "(BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) "
				+ "OR "
				+ "(BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') "
				+ "AND BEGIN_DATE < TO_DATE (?, 'yyyy-MM-dd') "
				+ "AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) "
				+ ") ";
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_begin_date());
			args.add(schoolBulletin.getWeek_end_date());
			args.add(schoolBulletin.getWeek_begin_date());

		} else if ("month".equals(schoolBulletin.getDate())) {
			sql += "AND "
				+ "( "
				+ "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ "OR "
				+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) "
				+ ") ";
		}
		sql += " ORDER BY SB.BEGIN_DATE DESC , SB.id DESC ";
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<SchoolBulletin>(SchoolBulletin.class), args.toArray());
	}

	
//	===================
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from school_bulletin";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(SchoolBulletin schoolBulletin) {
		String sql = " INSERT into school_bulletin (id,title,area,category,image,begin_date,end_date,click_rate,create_by,create_date,update_by,update_date,content,encrypturl,product_category) VALUES (:id,:title,:area,:category,:image,:begin_date,:end_date,:click_rate,:create_by,:create_date,:update_by,:update_date,:content,:encrypturl,:product_category)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET title = :title,area = :area,category = :category,image = :image,begin_date = :begin_date,end_date = :end_date,click_rate = :click_rate,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,content = :content,encrypturl = :encrypturl,product_category = :product_category "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(schoolBulletin);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}

	@Override
	public List<Map<String, Object>> getNormalAreaList() {
		String sql = "select * from area";
		
		return oracJdbcTemplate.queryForList(sql);
	}
	
	

	@Override
	public void updateNormalAreaData(Area area) {
		String sql = " INSERT into area (id,parent_id,name,layer,create_by,create_date,update_by,update_date,mapurl) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:mapUrl)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET parent_id = :parent_id,name = :name,layer = :layer,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = :update_date,mapurl = :mapUrl "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(area);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}

	

}
