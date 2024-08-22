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

import com.tkb.realgoodTransform.dao.NavBannerDao;
import com.tkb.realgoodTransform.model.NavBanner;




@Repository
public class NavBannerDaoImpl implements NavBannerDao {
	
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
	public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner) {
		
		List<Object> args = new ArrayList<Object>();
		String sql ="SELECT * FROM NAVBANNER WHERE 1=1 ";
		if ("1".equals(navBanner.getType())){
		    sql += " AND TYPE = '1'";
		}
		else if("2".equals(navBanner.getType()))
		{
		    sql += " AND TYPE = '2'";
		}

		if(navBanner.getTitle() != null && !"".equals(navBanner.getTitle())) {
			sql += " AND TITLE LIKE ? ";
			args.add("%" + navBanner.getTitle() + "%");
		}
		
		sql += " ORDER BY UPDATE_DATE DESC, SHOW DESC LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class), args.toArray());
	}

	@Override
	public List<NavBanner> getFrontList(NavBanner navBanner) {
		String sql = " SELECT * FROM NAVBANNER WHERE SHOW = 1 AND TYPE = '1' "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "	
				   + " ORDER BY RANDOM() LIMIT  3";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class));
	}

	@Override
	public List<NavBanner> getFrontType2List(NavBanner navBanner) {
		String sql = " SELECT * FROM NAVBANNER WHERE SHOW = 1 AND TYPE = '2' "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				   + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "	
				   + " ORDER BY RANDOM() LIMIT 1";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class));
	}

	@Override
	public Integer getCount(NavBanner navBanner) {
		/*
		 * 後台list頁面
		 * select選300*184與300*600串接
		 *   
		 */
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM NAVBANNER WHERE 1=1 ";
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
		String sql = "SELECT COUNT(*) FROM NAVBANNER WHERE SHOW = 1 AND ID = ?";
		args.add(navBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public NavBanner getData(NavBanner navBanner) {
		List<Object> args = new ArrayList<>();
		String sql ="SELECT * FROM NAVBANNER WHERE ID = ?";
		args.add(navBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM NAVBANNER ORDER BY ID DESC LIMIT 1 ";
		
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		
//		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		return nextId;
	}

	@Override
	public void add(NavBanner navBanner) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(navBanner);
		
		String sql = "INSERT INTO NAVBANNER(ID, IMAGE, TITLE, LINK, CLICK_RATE, BEGIN_DATE, END_DATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, SHOW, SORT, TYPE, SHOW_PAGE) "
				   +"VALUES(:id, :image, :title, :link, 0, :begin_date, :end_date, :create_by, now(), :update_by, now(), :show, :sort, :type, :show_page)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(NavBanner navBanner) {
		List<Object> args = new ArrayList<>();
		
		String sql = "UPDATE NAVBANNER SET UPDATE_BY = ?,UPDATE_DATE = NOW() ";
		args.add(navBanner.getUpdate_by());
			
		if(navBanner.getImage() != null) {
			sql += ",IMAGE = ? ";
			args.add(navBanner.getImage());
			}
		if(navBanner.getTitle() != null) {
			sql += ",TITLE = ? ";
			args.add(navBanner.getTitle());
		}
		if(navBanner.getLink() != null) {
			sql += ",LINK = ? ";
			args.add(navBanner.getLink());
		}
		if(navBanner.getShow() != null) {
			sql += ",SHOW = ? ";
			args.add(navBanner.getShow());
		}
		if(navBanner.getBegin_date() != null) {
			sql +=",BEGIN_DATE =? ";
			args.add(navBanner.getBegin_date());
		}
		if(navBanner.getEnd_date() != null) {
			sql +=",END_DATE = ? ";
			args.add(navBanner.getEnd_date());
		}
		if(navBanner.getType() != null) {
			sql +=",TYPE = ? ";
			args.add(navBanner.getType());
		}
		if(navBanner.getShow_page() != null) {
			sql +=",SHOW_PAGE = ? ";
			args.add(navBanner.getShow_page());
		}
		sql += " WHERE ID = ?";
		args.add(navBanner.getId());
		
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void updateClickRate(NavBanner navBanner) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE NAVBANNER SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(navBanner.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM NAVBANNER WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}

	@Override
	public List<NavBanner> getBrochureFrontList(NavBanner navBanner) {
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM NAVBANNER WHERE SHOW = 1"
				+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "	;
		
		//篩選顯示頁面
		if( "1".equals(navBanner.getShow_page()) ) {
			sql += "AND SHOW_PAGE LIKE '%1%'";
		}
		if( "2".equals(navBanner.getShow_page()) ) {
			sql += "AND SHOW_PAGE LIKE '%2%'";
		}
		if( "3".equals(navBanner.getShow_page()) ) {
			sql += "AND SHOW_PAGE LIKE '%3%'";
		}
		    
		sql += " ORDER BY RANDOM() LIMIT  3 ";
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class), args.toArray());
	}
	
	@Override
	public List<NavBanner> getBrochuregetFrontType2List(NavBanner navBanner) {
		List<Object> args = new ArrayList<Object>();
	    
	    String sql = " SELECT * FROM NAVBANNER WHERE SHOW = 1"
	        + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
	        + " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE " ;
	    
	    //篩選顯示頁面
		if( "1".equals(navBanner.getShow_page()) ) {
			sql += "AND SHOW_PAGE LIKE '%1%'";
		}
		if( "2".equals(navBanner.getShow_page()) ) {
			sql += "AND SHOW_PAGE LIKE '%2%'";
		}
		if( "3".equals(navBanner.getShow_page()) ) {
			sql += "AND SHOW_PAGE LIKE '%3%'";
		}
	        
	    sql += " ORDER BY RANDOM() LIMIT  1 ";
	    
	    return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<NavBanner>(NavBanner.class), args.toArray());
	}
	
	
//	=========================================
	
	@Override
	public List<Map<String, Object>> getNormalNavBannerList() {
		String sql = "select * from navbanner";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(NavBanner navBanner) {
		
		String sql = " INSERT into navbanner (id,image,title,link,click_rate,begin_date,end_date,create_by,create_date,update_by,update_date,show,sort,type,show_page) VALUES (:id,:image,:title,:link,:click_rate,:begin_date,:end_date,:create_by,:create_date,:update_by,CAST(:update_date AS TIMESTAMP),:show,:sort,:type,:show_page)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET image = :image, title = :title, link = :link, click_rate = :click_rate, begin_date = :begin_date, end_date = :end_date, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = CAST(:update_date AS TIMESTAMP), show = :show, sort = :sort, type = :type, show_page = :show_page "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(navBanner);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}

}
