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

import com.tkb.realgoodTransform.dao.GSATNewsDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.GSATNews;

/**
 * 外特高手經驗談Dao實作類
 */
@Repository
public class GSATNewsDaoImpl implements GSATNewsDao{
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgresqlJdbcTemplate;
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	@Autowired
	@Qualifier("fifthJdbcTemplate")
	private JdbcTemplate oracJdbcTemplate;
	
    //後台抓取清單
	@Override
	public List<GSATNews> getList(int pageCount, int pageStart, GSATNews gSATNews) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT  * FROM GSAT_NEWS WHERE 1=1";

		if(gSATNews.getCategory() != null && !"".equals(gSATNews.getCategory())) {
            sql += " AND CATEGORY LIKE ?";
            args.add("%"+gSATNews.getCategory()+"%");

        }
        if(gSATNews.getTitle() != null && !"".equals(gSATNews.getTitle())){
            sql += " AND TITLE LIKE ? ";
            args.add("%"+gSATNews.getTitle()+"%");

        }
		sql += " ORDER BY UPDATE_DATE DESC LIMIT ? OFFSET ?";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<GSATNews>(GSATNews.class), args.toArray());
		
	}
	
	@Override
	public List<GSATNews> getFrontList(GSATNews gSATNews) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_NEWS WHERE 1=1 AND SHOW='1' "
		            + " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
		            + " AND TO_DATE(TO_CHAR(now(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "
		            + " ORDER BY BEGIN_DATE DESC";
		
		
//		sql = " SELECT ROWNUM, NE.* FROM( " + sql +") NE WHERE ROWNUM < 4";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<GSATNews>(GSATNews.class), args.toArray());

		
	}
	
	@Override
	public List<GSATNews> getFrontList(int pageCount, int pageStart, GSATNews gSATNews, List<Area> areaList, String search_sort) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT G.*, C.CATEGORY AS CATEGORY_CATEGORY FROM GSAT G "
				   + " LEFT JOIN GSAT_CATEGORY C ON C.ID = G.CATEGORY WHERE 1 = 1 LIMIT ? OFFSET ?";

		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<GSATNews>(GSATNews.class), args.toArray());
		
	}
	
	@Override
	public List<GSATNews> getFrontList() {
		
		String sql = " SELECT N.*, C.CATEGORY AS CATEGORY_CATEGORY "
				   + " FROM GSAT G "
				   + " LEFT JOIN GSAT_CATEGORY C ON G.CATEGORY = C.ID "
				   + " WHERE 1 = 1 "
				   + " ORDER BY G.UPDATE_DATE DESC LIMIT 5";
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<GSATNews>(GSATNews.class));
		
	}
	//計算後台資料數量
	@Override
	public Integer getCount(GSATNews gSATNews) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_NEWS WHERE 1=1";
		
		if(gSATNews.getCategory() != null && !"".equals(gSATNews.getCategory())) {
            sql += " AND CATEGORY LIKE ?";
            args.add("%"+gSATNews.getCategory()+"%");
        }
		if(gSATNews.getTitle() != null && !"".equals(gSATNews.getTitle())) {
            sql += " AND TITLE LIKE ? ";
            args.add("%"+gSATNews.getTitle()+"%");
        }
		return postgresqlJdbcTemplate.queryForObject(sql,Integer.class,args.toArray());
		
	}

	//修改時抓取該ID資料	
	@Override
	public GSATNews getData(GSATNews gSATNews) {
		String sql = " SELECT * FROM GSAT_NEWS WHERE ID = ?";
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATNews>(GSATNews.class),new Object[]{ gSATNews.getId()});
		
	}
	
	public GSATNews getFrontData(GSATNews gSATNews) {
		
		String sql = " SELECT N.*, C.CATEGORY AS CATEGORY_CATEGORY "
				   + " FROM GSAT_NEWS N "
				   + " LEFT JOIN GSAT_CATEGORY C ON N.CATEGORY = C.ID "		
				   + " WHERE N.ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATNews>(GSATNews.class),new Object[]{ gSATNews.getId()});

	}
	
	//新增資料抓取下一個ID	
	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_NEWS ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
		
	}
	
	//新增功能	
	@Override
	public void add(GSATNews gSATNews) {
		String sql = " INSERT INTO GSAT_NEWS "
				   + " (ID,CATEGORY,TITLE, CONTENT, SHOW, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, BEGIN_DATE, END_DATE) "
				   + " VALUES(?, ?, ?, ?, ?, ?, now(), ?, now(), ?, ?) ";
		
		postgresqlJdbcTemplate.update(sql, new Object[] { gSATNews.getId(),gSATNews.getCategory(),
				gSATNews.getTitle(),gSATNews.getContent(),gSATNews.getShow(),
				gSATNews.getCreate_by(), gSATNews.getUpdate_by(),gSATNews.getBegin_date(),
				gSATNews.getEnd_date()});
		
	}
	
	//修改功能	
	@Override
	public void update(GSATNews gSATNews) {
		String sql = " UPDATE GSAT_NEWS SET TITLE = ?,CATEGORY = ?,"
				   + " SHOW = ?, CONTENT = ?, UPDATE_BY = ?, UPDATE_DATE = now() ,BEGIN_DATE = ?, END_DATE = ? WHERE ID = ? ";
		
		postgresqlJdbcTemplate.update(sql, new Object[] { 
				gSATNews.getTitle(),gSATNews.getCategory(),
				gSATNews.getShow(),gSATNews.getContent(),
				gSATNews.getUpdate_by(),gSATNews.getBegin_date(),gSATNews.getEnd_date(),
				gSATNews.getId() });
		
	}
	
	@Override
	public void updateClickRate(GSATNews gSATNews) {
		
		String sql = " UPDATE GSAT_NEWS SET CLICK_RATE = CLICK_RATE+1 WHERE ID = ? ";
		
		postgresqlJdbcTemplate.update(sql, new Object[] { gSATNews.getId() });
		
	}
	
	//刪除功能	
	@Override
	public void delete(GSATNews gSATNews ,Integer id) {

		String sql = " DELETE FROM GSAT_NEWS WHERE ID = ? ";
		
		postgresqlJdbcTemplate.update(sql, new Object[] { id });
		
	}
	
	
	
//	===========================
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from GSAT_NEWS";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}
	@Override
	public void updateNormalData(GSATNews gSATNews) {
		String sql = " INSERT into gsat_news (id,title,category,show,create_by,create_date,update_by,update_date,begin_date,end_date,content) VALUES (:id,:title,:category,:show,:create_by,:create_date,:update_by,:update_date,:begin_date,:end_date,:content)  "
					+ " ON CONFLICT(id) "
					+ " DO UPDATE SET title = :title, category = :category, show = :show, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, begin_date = :begin_date, end_date = :end_date, content = :content "; 
		
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(gSATNews);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}
	
}
