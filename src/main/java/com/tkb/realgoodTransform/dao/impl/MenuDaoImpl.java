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

import com.tkb.realgoodTransform.dao.MenuDao;
import com.tkb.realgoodTransform.model.Menu;
import com.tkb.realgoodTransform.model.User;

/**
 * 選單Dao實作類
 * @author Joshua
 * @version 創建時間：2022-01-25
 */
@Repository
public class MenuDaoImpl implements MenuDao {
	
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
		String sql = "SELECT * FROM MENU";
		return fifthJdbcTemplate.queryForList(sql);
	}
	@Override
	public List<Map<String, Object>> getListForChecktData() {
		String sql = "SELECT * FROM MENU";
		List<Map<String, Object>> list = postgresqlJdbcTemplate.queryForList(sql);
		if (list != null) {
			return list;
		} else {
			return null;
		}
	}
	@Override
	public void insertForRemake(Menu menu) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(menu);
		String sql = "INSERT INTO MENU"
				+ " (ID, PARENT_ID, NAME, LAYER, LINK, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				+ " VALUES(:id, :parent_id, :name, :layer, :link, :create_by, :create_date, :update_by, :update_date)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}
	@Override
	public void updateForRemake(Menu menu) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(menu);
		String sql = "UPDATE MENU SET "
		           + "PARENT_ID = :parent_id, "
		           + "NAME = :name, "
		           + "LAYER = :layer, "
		           + "LINK = :link, "
		           + "CREATE_BY = :create_by, "
		           + "CREATE_DATE = :create_date, "
		           + "UPDATE_BY = :update_by, "
		           + "UPDATE_DATE = :update_date "
		           + "WHERE ID = :id";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}
	// ======================================== 開發用method end =========================================

	@Override
	public Integer getCount(Menu menu) {
		
//		SqlParameterSource parameters = new BeanPropertySqlParameterSource(menu);
		List<Object> args = new ArrayList<Object>();
		
//		String sql = " SELECT COUNT(*) FROM MENU ";
		String sql = " SELECT COUNT(*) FROM MENU WHERE LAYER = ? ";
		
		if(menu.getLayer() == null) {
			menu.setLayer("1");
		}
		
		args.add(menu.getLayer());
		
		if(menu.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(menu.getParent_id());
		}
		
//		return postgresqlJdbcNameTemplate.queryForObject(sql, parameters, Integer.class);
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public List<Menu> getList(int pageCount, int pageStart, Menu menu) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM MENU WHERE LAYER = ? ";
//		String sql = " SELECT * FROM MENU ";
		
		if(menu.getLayer() == null) {
			menu.setLayer("1");
		}
		
		args.add(menu.getLayer());
		
		if(menu.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(menu.getParent_id());
		}
		
		sql += " ORDER BY ID ";
		
		sql += " LIMIT ? OFFSET ? ";
		
		args.add(pageCount);
		args.add(pageStart);
		
//		return postgresqlJdbcTemplate.query(sql, args.toArray(), getRowMapper());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		
		Integer nextId = null;
		String sql = " SELECT ID FROM MENU ORDER BY ID DESC LIMIT 1 ";
		
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		
//		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		return nextId;
	}

	@Override
	public void add(Menu menu) {
		
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(menu);
		
		String sql = " INSERT INTO MENU(ID, PARENT_ID, NAME, LAYER, LINK, CREATE_BY, "
				   + " UPDATE_BY, UPDATE_DATE) "
				   + " VALUES "
				   + " (:id, :parent_id, :name, :layer, :link, :create_by, :update_by, now()) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
		
	}

	@Override
	public Menu getData(Menu menu) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM MENU WHERE ID = ? ";
		
		args.add(menu.getId());
		
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Menu>(Menu.class), args.toArray());
	}

	@Override
	public void update(Menu menu) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE MENU SET NAME = ?, LINK = ?, UPDATE_BY = ?, UPDATE_DATE = NOW() "
				   + " WHERE ID = ? ";
		
		args.add(menu.getName());
		args.add(menu.getLink());
		args.add(menu.getUpdate_by());
		args.add(menu.getId());
		
		postgresqlJdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public List<Menu> getLayer1List(User user) {
		
		List<Object> args = new ArrayList<>();
		
//		String sql = " SELECT * FROM MENU WHERE LAYER = ?";
//		args.add("1");
		String sql = "SELECT M2.* FROM GROUP_ACTION GA "
				   + " LEFT JOIN GROUPS G ON GA.GROUP_ID = G.ID"
				   + " LEFT JOIN MENU M ON M.ID = GA.ACTION_ID "
				   + " LEFT JOIN MENU M2 ON M2.ID = M.PARENT_ID "
				   + " LEFT JOIN GROUP_USER GU ON GU.GROUP_ID = G.ID "
				   + " LEFT JOIN USER_ACCOUNT UA ON UA.ID = GU.USER_ID "
				   + " WHERE M2.LAYER = ? "
				   + " AND UA.ACCOUNT = ? "
				   + " GROUP BY M2.ID ";
		args.add("1");
		args.add(user.getAccount());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class), args.toArray());
	}

	@Override
	public List<Menu> getLayer2List(Menu menu) {
		
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(menu);
		
		String sql = " SELECT * FROM MENU WHERE LAYER = '2' AND PARENT_ID = :parent_id ORDER BY ID ";
		
		return postgresqlJdbcNameTemplate.query(sql, parameters, new BeanPropertyRowMapper<Menu>(Menu.class));
	}

	@Override
	public List<Menu> getSubList(Menu menu) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM MENU WHERE PARENT_ID = ? "
				   + " ORDER BY ID ";
		
		args.add(menu.getParent_id());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class), args.toArray());
	}

	@Override
	public void delete(int id) {
		
		String sql = " DELETE FROM MENU WHERE ID = ? ";
		
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
		
	}

	@Override
	public List<Menu> getLayer2AllList(Menu menu) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM MENU WHERE LAYER = ? ORDER BY ID";
		args.add(menu.getLayer());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class), args.toArray());
	}

	@Override
	public List<Map<String, Object>> getMenuLayer2List(Menu menu, Integer group_id) {
		List<Object> args = new ArrayList<>();
		String sql = "";
		if (group_id != 0) {
			sql = "SELECT m.id AS id, m.name AS name, ga.group_id AS group_id, ga.aciton_id AS group_aciton_id "
					+ "FROM menu m "
					+ "LEFT JOIN group_action ga ON ga.aciton_id = m.id AND ga.group_id = ? "
					+ "WHERE m.layer = ? "
					+ "GROUP BY 1,3,4 "
					+ "ORDER BY m.id";
			args.add(group_id);
			args.add(menu.getLayer());
		}else {
			sql = "SELECT id, name FROM menu WHERE LAYER = ? ORDER BY id";
			args.add("2");
		}
		return postgresqlJdbcTemplate.queryForList(sql, args.toArray());
	}

}
