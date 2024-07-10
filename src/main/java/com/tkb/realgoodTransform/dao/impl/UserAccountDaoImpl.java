package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.UserAccountDao;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;




@Repository
public class UserAccountDaoImpl implements UserAccountDao {
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;

	@Override
	//tkbApi新增全部使用者名單(初始化)
	public void insert(UserAccount userAccount) {
		
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(userAccount);
		
		String sql = "INSERT INTO USER_ACCOUNT"
				+ "(ID, ACCOUNT, CHINESE_NAME, DEPARTMENT_NO, UNIT_NO, EMAIL, CREATE_BY)"
				+ "VALUES"
				+ "(:user_account_id, :employee_no, :employee_name, :department_id, :unit_id, :email, :create_by)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public int getCount(User user) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM USER_ACCOUNT ";
		
		if(user.getUnit_no() != null && !"".equals(user.getUnit_no())) {
			sql += " WHERE DEPARTMENT_NO = ? AND UNIT_NO = ? ";
			args.add(user.getDepartment_no());
			args.add(user.getUnit_no());
		} else {
			sql += " WHERE DEPARTMENT_NO = ? ";
			args.add(user.getDepartment_no());
		}
		
		sql += " AND (STATUS <> '2' OR STATUS IS NULL)";

		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public List<User> getList(int pageCount, int pageStart, User user) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT UA.*, GU.GROUP_ID AS GROUP_ID, G.NAME AS GROUPS_NAME "
				+ " FROM USER_ACCOUNT AS UA "
				+ " LEFT JOIN GROUP_USER AS GU ON GU.USER_ID = UA.ID "
				+ " LEFT JOIN GROUPS AS G ON G.ID = GU.GROUP_ID ";
		
		if(user.getUnit_no() != null && !"".equals(user.getUnit_no())) {
			sql += " WHERE UA.DEPARTMENT_NO = ? AND UA.UNIT_NO = ? ";
			args.add(user.getDepartment_no());
			args.add(user.getUnit_no());
		} else {
			sql += " WHERE UA.DEPARTMENT_NO = ? ";
			args.add(user.getDepartment_no());
		}
		
		sql += " AND (STATUS <> '2' OR STATUS IS NULL) ";
		
		sql += " ORDER BY UA.ACCOUNT ";
		
		sql += " LIMIT ? OFFSET ? ";
		
		args.add(pageCount);
		args.add(pageStart);
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class), args.toArray());
		
	}
	
	@Override
	public User getDataByAccount(User user) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM USER_ACCOUNT WHERE ACCOUNT = ?";
		args.add(user.getAccount());
		try {
			return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class) ,args.toArray());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User getDataById(User user) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(user);
		String sql = "SELECT * FROM USER_ACCOUNT WHERE ID = :id";
		return postgresqlJdbcNameTemplate.queryForObject(sql, parameter, new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public void update(User user) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(user);
		String sql = "UPDATE USER_ACCOUNT SET STATUS = :status, UPDATE_BY = :update_by, UPDATE_DATE = now(), AREA = :area"
				+ " WHERE id = :id";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public List<String> getUserMenuList(User userAccountSession, String link) {
		String sql = "SELECT M.LINK "
				+ " FROM GROUP_ACTION as GA "
				+ " LEFT JOIN menu as m ON GA.aciton_id = m.id "
				+ " WHERE GA.GROUP_ID = "
				+ " ( SELECT GROUP_ID FROM GROUP_USER WHERE USER_ID = "
				+ " (SELECT ID FROM USER_ACCOUNT WHERE ACCOUNT = UPPER(?) ) ) "
				+ " AND m.LINK = ? ";
		return postgresqlJdbcTemplate.queryForList(sql, String.class, new Object[]{userAccountSession.getAccount(), link});
	}

	@Override
	public User login(User user) {
		String sql = "SELECT * FROM USER_ACCOUNT WHERE ACCOUNT = upper(?) ";
		try {
			return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), user.getAccount());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void updateStatus(User user) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(user);
		String sql = " UPDATE user_account SET STATUS = :status, UPDATE_BY = :update_by, UPDATE_DATE = now() "
				+ " WHERE ACCOUNT = :account ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	@Override
	public void scheduleUpdate(User user) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(user);
		String sql = "UPDATE user_account "
				   + "SET chinese_name = :chinese_name, department_no = :department_no, "
				   + "unit_no = :unit_no, update_by = :update_by, update_date = now() "
				   + "WHERE account = :account ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}
	
	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM user_account ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			e.printStackTrace();
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public Map<String, Object> getLocationNoByAccount(String account) {
		String sql = "SELECT location_no FROM user_account WHERE account = ?";
		Map<String, Object> queryForMap = postgresqlJdbcTemplate.queryForMap(sql,account);
		return queryForMap;
	}

	@Override
	public void insertAndUpdate(User user) {

		String sql = "INSERT INTO user_account (account, chinese_name, department_no, department_name, unit_no, unit_name, location_no, email, status, update_by, update_date) VALUES (:account, :chinese_name, :department_no, :department_name, :unit_no, :unit_name, :location_no, :email, :status, :update_by, now()) "
				+ "	ON CONFLICT (account) "
				+ " DO UPDATE SET account = :account, chinese_name = :chinese_name, department_no = :department_no, department_name = :department_name, unit_no = :unit_no, unit_name = :unit_name, location_no = :location_no, email = :email, status = :status, update_by = :update_by, update_date = now()  ";
		
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(user);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, sqlParameterSource, keyHolder);
	}

	@Override
	public List<Map<String, Object>> getEmployeeList(User user) {
		List<Object> args = new ArrayList<>();
		
		String sql = "select * from user_account where department_no = ? and unit_no = ? and status = '1' order by id ASC ";
		
		args.add(user.getDepartment_no());
		args.add(user.getUnit_no());
		
		return postgresqlJdbcTemplate.queryForList(sql, args.toArray());
	}

	@Override
	public void updateLeaveUserStatus(String employee_no) {
		MapSqlParameterSource params = new MapSqlParameterSource();
	    String sql = "UPDATE user_account SET status = '0',update_by = 'SYSTEM_LEAVE',update_date = now() WHERE account = :account";
	    params.addValue("account", employee_no);

	    postgresqlJdbcNameTemplate.update(sql, params);
	}

}
   