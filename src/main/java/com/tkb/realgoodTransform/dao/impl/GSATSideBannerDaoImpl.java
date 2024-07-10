package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;

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

import com.tkb.realgoodTransform.dao.GSATSideBannerDao;
import com.tkb.realgoodTransform.model.NavBanner;

/**
 * nav Banner Dao實作類
 */
@Repository
public class GSATSideBannerDaoImpl implements GSATSideBannerDao {
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;

	@Override
	public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner) {
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM GSAT_SIDE_BANNER WHERE 1 = 1 ";
		if ("1".equals(navBanner.getType())) {
			sql += " AND TYPE = '1'";
		} else if ("2".equals(navBanner.getType())) {
			sql += " AND TYPE = '2'";
		}
		if (navBanner.getTitle() != null && !"".equals(navBanner.getTitle())) {
			sql += " AND TITLE LIKE ? ";
			args.add("%" + navBanner.getTitle() + "%");
		}
		sql += " ORDER BY ID LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class), args.toArray());

	}

	/*
	 * 取所有300*184圖片顯示前台且開始時間與結束時間符合 隨機排序 最多取4個
	 */
	@Override
	public List<NavBanner> getFrontList(NavBanner navBanner) {
		String sql = " SELECT * FROM GSAT_SIDE_BANNER WHERE SHOW = 1 AND CAST(TYPE AS INTEGER) = 1 "
				+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "
				+ " ORDER BY RANDOM() LIMIT  3";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class));

	}

	/*
	 * 取300*600 最多取1個 隨機取
	 */
	@Override
	public List<NavBanner> getFrontType2List(NavBanner navBanner) {
		String sql = " SELECT * FROM GSAT_SIDE_BANNER WHERE SHOW = 1 AND CAST(TYPE AS INTEGER) = 2 "
				+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "
				+ " ORDER BY RANDOM() LIMIT 1";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class));

	}

	@Override
	public Integer getCount(NavBanner navBanner) {
		/*
		 * 後台list頁面 select選300*184與300*600串接
		 */
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM GSAT_SIDE_BANNER WHERE 1=1 ";
		if ("1".equals(navBanner.getType())){
            sql += " AND TYPE = '1'";
        }
        else if("2".equals(navBanner.getType()))
        {
            sql += " AND TYPE = '2'";
        }
		if(null!=navBanner.getTitle()&&!"".equals(navBanner.getTitle())) {
			sql +="AND TITLE LIKE ?";
			args.add("%" + navBanner.getTitle() + "%");
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public Integer getFrontCount(NavBanner navBanner) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM GSAT_SIDE_BANNER WHERE SHOW = 1 AND ID = ?";
		args.add(navBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public Integer getShowCount(NavBanner navBanner) {
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT COUNT(*) FROM GSAT_SIDE_BANNER WHERE SHOW = 1";
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public NavBanner getData(NavBanner navBanner) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM GSAT_SIDE_BANNER WHERE ID = ?";
		args.add(navBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class),
				args.toArray());

	}

	@Override
	public NavBanner getFrontData(NavBanner navBanner) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM GSAT_SIDE_BANNER WHERE ID = ?";
		args.add(navBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class),
				args.toArray());

	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_SIDE_BANNER ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(NavBanner navBanner) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(navBanner);
		String sql = "INSERT INTO GSAT_SIDE_BANNER(ID, IMAGE, TITLE, LINK, CLICK_RATE, BEGIN_DATE, END_DATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, SHOW, SORT, TYPE) "
				+ "VALUES(:id, :image, :title, :link, 0, :begin_date, :end_date, :create_by, :create_date, :update_by, :update_date, :show, :sort, :type)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(NavBanner navBanner) {
		List<Object> args = new ArrayList<>();

		String sql = "UPDATE GSAT_SIDE_BANNER SET UPDATE_BY = ?,UPDATE_DATE = NOW() ";
		args.add(navBanner.getUpdate_by());

		if (navBanner.getImage() != null) {
			sql += ",IMAGE = ? ";
			args.add(navBanner.getImage());
		}
		if (navBanner.getTitle() != null) {
			sql += ",TITLE = ? ";
			args.add(navBanner.getTitle());
		}
		if (navBanner.getLink() != null) {
			sql += ",LINK = ? ";
			args.add(navBanner.getLink());
		}
		if (navBanner.getShow() != null) {
			sql += ",SHOW = ? ";
			args.add(navBanner.getShow());
		}
		if (navBanner.getBegin_date() != null) {
			sql += ",BEGIN_DATE =? ";
			args.add(navBanner.getBegin_date());
		}
		if (navBanner.getEnd_date() != null) {
			sql += ",END_DATE = ? ";
			args.add(navBanner.getEnd_date());
		}
		if (navBanner.getType() != null) {
			sql += ",TYPE = ? ";
			args.add(navBanner.getType());
		}
		sql += " WHERE ID = ?";
		args.add(navBanner.getId());

		postgresqlJdbcTemplate.update(sql, args.toArray());

	}

	public void updateEditor(NavBanner navBanner) {
		String sql = "UPDATE GSAT_SIDE_BANNER SET UPDATE_BY = ?, UPDATE_DATE = now()";
		postgresqlJdbcTemplate.update(sql, new Object[] { navBanner.getUpdate_by() });
	}

	@Override
	public void updateClickRate(NavBanner navBanner) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE GSAT_SIDE_BANNER SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(navBanner.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());

	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM GSAT_SIDE_BANNER WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}

	@Override
	public void updateSort(NavBanner navBanner) {
		
	}

	@Override
	public void resetSort() {
		
	}

}
