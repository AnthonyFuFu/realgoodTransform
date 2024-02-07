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
	
	@Autowired
    @Qualifier("fifthJdbcTemplate")
	private JdbcTemplate fifthJdbcTemplate;

	// ======================================== 開發用method start ========================================= 
	@Override
	public List<Map<String, Object>> getListForInsertData() {
		String sql = "SELECT * FROM USER_ACCOUNT";
		return fifthJdbcTemplate.queryForList(sql);
	}
	@Override
	public List<Map<String, Object>> getListForChecktData() {
		String sql = "SELECT * FROM USER_ACCOUNT";
		List<Map<String, Object>> list = postgresqlJdbcTemplate.queryForList(sql);
		if (list != null) {
			return list;
		} else {
			return null;
		}
	}
	@Override
	public void insertForRemake(UserAccount userAccount) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(userAccount);
		String sql = "INSERT INTO USER_ACCOUNT"
				+ " (ID, ACCOUNT, CHINESE_NAME, DEPARTMENT_NO, UNIT_NO, EMAIL, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, AREA) "
				+ " VALUES(:id, :account, :chinese_name, :department_no, :unit_no, :email, :status, :create_by, :create_date, :update_by, :update_date, :area)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}
	@Override
	public void updateForRemake(UserAccount userAccount) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(userAccount);
		String sql = "UPDATE USER_ACCOUNT SET "
		           + "ACCOUNT = :account, "
		           + "CHINESE_NAME = :chinese_name, "
		           + "DEPARTMENT_NO = :department_no, "
		           + "UNIT_NO = :unit_no, "
		           + "EMAIL = :email, "
		           + "STATUS = :status, "
		           + "CREATE_BY = :create_by, "
		           + "CREATE_DATE = :create_date, "
		           + "UPDATE_BY = :update_by, "
		           + "UPDATE_DATE = :update_date, "
		           + "AREA = :area "
		           + "WHERE ID = :id";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}
	// ======================================== 開發用method end =========================================

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
	public void insert(UserAccount userAccount) {

		SqlParameterSource parameter = new BeanPropertySqlParameterSource(userAccount);

		String sql = "INSERT INTO USER_ACCOUNT"
				+ "(ACCOUNT, CHINESE_NAME, DEPARTMENT_NO, UNIT_NO, EMAIL, CREATE_BY)"
				+ "VALUES"
				+ "(:employee_no, :employee_name, :department_id, :unit_id, :email, :create_by)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	
	
	@Override
	public List<String> getUserMenuList(User userAccountSession, String link) {
		String sql = "SELECT M.LINK "
				+ " FROM GROUP_ACTION as GA "
				+ " LEFT JOIN menu as m ON GA.aciton_id = m.id "
				+ " WHERE GA.GROUP_ID = "
				+ " ( SELECT GROUP_ID FROM GROUP_USER WHERE USER_ID = "
				+ " (SELECT ID FROM USER_ACCOUNT WHERE ACCOUNT = ?) ) "
				+ " AND m.LINK = ? ";
		return postgresqlJdbcTemplate.queryForList(sql, String.class, new Object[]{userAccountSession.getAccount(), link});
	}
	
	@Override
	public User login(User user) {
		String sql = "SELECT * FROM USER_ACCOUNT WHERE ACCOUNT = upper(?) "
				+ " AND STATUS = '1' ";
		try {
			return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), user.getAccount());
		} catch (Exception e) {
			return null;
		}
	}

	
}
