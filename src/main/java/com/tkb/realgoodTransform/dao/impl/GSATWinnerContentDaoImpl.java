package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.GSATWinnerContentDao;
import com.tkb.realgoodTransform.model.GSATWinnerContent;

/**
 * 學堂公告內容Dao實作類
 */
@Repository
public class GSATWinnerContentDaoImpl  implements GSATWinnerContentDao {
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgreJdbcTemplate;
	
	public List<GSATWinnerContent> getList(GSATWinnerContent gSATWinnerContent) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_WINNER_CONTENT WHERE WINNER_ID = ? ";
		args.add(gSATWinnerContent.getWinner_id());
		
		sql += " ORDER BY ID ";
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinnerContent>(GSATWinnerContent.class),args.toArray());
		
	}
	
	public List<GSATWinnerContent> getList(int pageCount, int pageStart, GSATWinnerContent gSATWinnerContent) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_WINNER_CONTENT WHERE ID <> '1' ";
		
//		if(gSATWinnerContent.getTitle() != null && !"".equals(gSATWinnerContent.getTitle())) {
//			sql += " WHERE TITLE LIKE ? ";
//			args.add("%" + gSATWinnerContent.getTitle() + "%");
//		}
		
		sql += " ORDER BY ID LIMIT ? OFFSET ?";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinnerContent>(GSATWinnerContent.class),args.toArray());

		
	}
	
	public Integer getCount(GSATWinnerContent gSATWinnerContent) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_WINNER_CONTENT WHERE ID <> '1' ";
		
//		if(gSATWinnerContent.getTitle() != null && !"".equals(gSATWinnerContent.getTitle())) {
//			sql += " WHERE TITLE LIKE ? ";
//			args.add("%" + gSATWinnerContent.getTitle() + "%");
//		}
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
		
	}
	
	public GSATWinnerContent getData(GSATWinnerContent gSATWinnerContent) {
		
		String sql = " SELECT * FROM GSAT_WINNER_CONTENT WHERE ID = ? ";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATWinnerContent>(GSATWinnerContent.class),new Object[] { gSATWinnerContent.getId() });

	}
	
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_WINNER_CONTENT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
		
		
	}
	
	public void add(GSATWinnerContent gSATWinnerContent) {
	    
		String sql = " INSERT INTO GSAT_WINNER_CONTENT(ID, WINNER_ID, TITLE, CONTENT, CREATE_BY, "
				   + " CREATE_DATE, UPDATE_BY, UPDATE_DATE)VALUES(?, ?,  ?, ?, ?, now(), ?, now()) ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATWinnerContent.getId(), gSATWinnerContent.getWinner_id(), 
				gSATWinnerContent.getTitle(), gSATWinnerContent.getContent(),
				gSATWinnerContent.getCreate_by(), gSATWinnerContent.getUpdate_by() });
		
	}
	
	public void update(GSATWinnerContent gSATWinnerContent) {
		
		String sql = " UPDATE GSAT_WINNER_CONTENT SET TITLE = ?, CONTENT = ?, "
				   + " UPDATE_BY = ?, UPDATE_DATE = now() WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] {  gSATWinnerContent.getTitle(),
				gSATWinnerContent.getContent(), gSATWinnerContent.getUpdate_by(), gSATWinnerContent.getId() });
		
	}
	
	public void delete(Integer id) {
		
		String sql = " DELETE FROM GSAT_WINNER_CONTENT WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { id });
		
	}
	
}
