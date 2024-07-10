package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.GSATWinnerDao;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.GSATWinner;

/**
 * 外特高手經驗談Dao實作類
 */
@Repository
public class GSATWinnerDaoImpl implements GSATWinnerDao{
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgreJdbcTemplate;
	
    //後台抓取清單
	@Override
	public List<GSATWinner> getList(int pageCount, int pageStart, GSATWinner gSATWinner) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT  * FROM GSAT_WINNER WHERE 1 = 1 ";
		
		if(gSATWinner.getTitle() != null && !"".equals(gSATWinner.getTitle())) {
			sql += " AND TITLE LIKE ? ";
			args.add("%" + gSATWinner.getTitle() + "%");		
		}
		
		if(gSATWinner.getName() != null && !"".equals(gSATWinner.getName())) {
			sql += " AND NAME LIKE ? ";
			args.add("%" + gSATWinner.getName() + "%");	
		}
		
		sql += " ORDER BY SORT LIMIT ? OFFSET ?";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),args.toArray());

	}
	
	@Override
	public List<GSATWinner> getFrontList(GSATWinner gSATWinner) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_WINNER WHERE 1=1 AND SHOW='1' ";
		
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),args.toArray());
		
	}
	
	@Override
	public List<GSATWinner> getFrontList(int pageCount, int pageStart, GSATWinner gSATWinner) {
		
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM GSAT_WINNER WHERE 1=1 AND SHOW='1' ";
		sql +="ORDER BY SORT LIMIT ? OFFSET ?";
		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),args.toArray());
		
	}
	
	@Override
	public List<GSATWinner> getFrontList(int pageCount, int pageStart, GSATWinner gSATWinner, List<Area> areaList, String search_sort) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT NE.*, "
				   + " C.NAME AS CATEGORY_NAME FROM GSAT NE "
				   + " LEFT JOIN GSAT_CATEGORY C ON C.ID = NE.CATEGORY WHERE 1 = 1 LIMIT ? OFFSET ?";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),args.toArray());
		
	}
	
	@Override
	public List<GSATWinner> getFrontList() {
		
		String sql = " SELECT N.*, NC.NAME AS CATEGORY_NAME "
				   + " FROM GSAT N "
				   + " LEFT JOIN GSAT_CATEGORY NC ON N.CATEGORY = NC.ID "
				   + " WHERE 1 = 1 "			   
				   + " ORDER BY N.UPDATE_DATE DESC LIMIT 5";
		
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class));
		
	}
	
	//計算後台資料數量
	@Override
	public Integer getCount(GSATWinner gSATWinner) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_WINNER WHERE 1 = 1 ";
		
		if(gSATWinner.getTitle() != null && !"".equals(gSATWinner.getTitle())) {
			sql += " AND TITLE LIKE ? ";
			args.add("%" + gSATWinner.getTitle() + "%");		
		}
		
		if(gSATWinner.getName() != null && !"".equals(gSATWinner.getName())) {
			sql += " AND NAME LIKE ? ";
			args.add("%" + gSATWinner.getName() + "%");	
		}
		
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());

	}
	@Override
	public Integer getFrontCount(GSATWinner gSATWinner) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_WINNER "
				   + " WHERE 1 = 1 AND SHOW =1";

		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
		
	}
	
	//修改時抓取該ID資料
	@Override
	public GSATWinner getData(GSATWinner gSATWinner) {
		String sql = " SELECT * FROM GSAT_WINNER WHERE ID = ?";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),new Object[] { gSATWinner.getId() });
	}
	
	@Override
	public GSATWinner getFrontData(GSATWinner gSATWinner) {
		
		String sql = " SELECT N.*, NC.NAME AS CATEGORY_NAME "
				   + " FROM GSAT_WINNER N "
				   + " LEFT JOIN GSAT_CATEGORY NC ON N.CATEGORY = NC.ID "		
				   + " WHERE N.ID = ? ";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),new Object[] { gSATWinner.getId() });

	}
	
	//新增資料抓取下一個ID
	@Override
	public Integer getNextId() {
		
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_WINNER ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
		
	}
	
	//新增功能
	@Override
	public void add(GSATWinner gSATWinner) {
		addSort(gSATWinner);
		String sql = " INSERT INTO GSAT_WINNER(ID,NAME,TITLE,"
				   + " CONTENT,SORT,SHOW,CREATE_BY, CREATE_DATE,"
				   + " UPDATE_BY, UPDATE_DATE)VALUES(?, ?,?, ?, ?,?,?,"
				   + " now(), ?, now()) ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATWinner.getId(),gSATWinner.getName(),
				gSATWinner.getTitle(),gSATWinner.getContent(),
				gSATWinner.getSort(),gSATWinner.getShow(),
				gSATWinner.getCreate_by(), gSATWinner.getUpdate_by()});
		
	}
	//修改功能
	@Override
	public void update(GSATWinner gSATWinner) {
	  sort(gSATWinner);
		String sql = " UPDATE GSAT_WINNER SET TITLE = ?,NAME = ?,"
				   + " SHOW = ?, CONTENT = ?, SORT = ?, UPDATE_BY = ?,"
				   + " UPDATE_DATE = now() WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { 
				gSATWinner.getTitle(),gSATWinner.getName(),
				gSATWinner.getShow(),
				gSATWinner.getContent(), gSATWinner.getSort(),
				gSATWinner.getUpdate_by(), gSATWinner.getId() });
		
	}
	@Override
	public void updateClickRate(GSATWinner gSATWinner) {
		
		String sql = " UPDATE GSAT_WINNER SET CLICK_RATE = CLICK_RATE+1 WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATWinner.getId() });
		
	}
	//刪除功能
	@Override
	public void delete(GSATWinner gSATWinner ,Integer id) {
		deleteSort(gSATWinner);
		String sql = " DELETE FROM GSAT_WINNER WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { id });
		
	}	
	
	public void addSort(GSATWinner gSATWinner){
		String sql = "UPDATE GSAT_WINNER SET SORT = SORT +1 WHERE SORT >= ? ";
		postgreJdbcTemplate.update(sql, new Object[]{ gSATWinner.getSort()});
	}
	
	public void sort(GSATWinner gSATWinner) {
		String sql = "SELECT COUNT(*) FROM GSAT_WINNER WHERE SORT = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSATWinner.getSort()});

		//若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT SORT FROM GSAT_WINNER WHERE ID = ?";
			
			GSATWinner oldGSATWinner = postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),new Object[] {gSATWinner.getId()});
			
			if (gSATWinner.getSort() < oldGSATWinner.getSort()) {
				
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE GSAT_WINNER SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ?";
				postgreJdbcTemplate.update(sql, new Object[]{ oldGSATWinner.getSort(), gSATWinner.getSort()});
			} else if (gSATWinner.getSort() > oldGSATWinner.getSort()){
				
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE GSAT_WINNER SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ?";
				postgreJdbcTemplate.update(sql, new Object[]{ gSATWinner.getSort(), oldGSATWinner.getSort()});
			}			
		}
		sql = "UPDATE GSAT_WINNER SET SORT = ? WHERE ID = ?";
		postgreJdbcTemplate.update(sql, new Object[]{gSATWinner.getSort(), gSATWinner.getId()});
	}	
	
	public void deleteSort(GSATWinner gSATWinner){
		String sql1 = "SELECT SORT FROM GSAT_WINNER WHERE ID = ?";
		
		GSATWinner oldGSATWinner = postgreJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),new Object[] {gSATWinner.getId()});

		String sql = "UPDATE GSAT_WINNER SET SORT = SORT -1 WHERE SORT > ?";

		postgreJdbcTemplate.update(sql, new Object[]{ oldGSATWinner.getSort()});
	}

	
	
}
