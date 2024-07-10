package com.tkb.realgoodTransform.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tkb.realgoodTransform.dao.GSATAdmitDao;
import com.tkb.realgoodTransform.model.GSATAdmit;
import com.tkb.realgoodTransform.model.GSATWinner;

/**
 * 外特高手經驗談Dao實作類
 */
@Repository
public class GSATAdmitDaoImpl implements GSATAdmitDao{
	
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgreJdbcTemplate;

	@Override
	public List<GSATAdmit> getList(int pageCount, int pageStart, GSATAdmit gSATAdmit) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT  * FROM GSAT_ADMIT";

		sql += " ORDER BY CREATE_DATE DESC LIMIT ? OFFSET ?";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATAdmit>(GSATAdmit.class),args.toArray());
	}
	
	@Override
	public List<GSATAdmit> getFrontList(GSATAdmit gSATAdmit) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_ADMIT";
		
		sql +=" ORDER BY CREATE_DATE DESC LIMIT 10";
		
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATAdmit>(GSATAdmit.class),args.toArray());
		
	}
	
	@Override
	public Integer getCount(GSATAdmit gSATAdmit) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_ADMIT ";
		
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
		
	}
	
	@Override
	public GSATAdmit getData(GSATAdmit gSATAdmit) {
		String sql = " SELECT * FROM GSAT_ADMIT WHERE ID = ?";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATAdmit>(GSATAdmit.class),new Object[] {gSATAdmit.getId()});
	}
	
	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_ADMIT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}
	
	@Override
	public void add(GSATAdmit gSATAdmit) {
//	  addSort(gSATAdmit);
		String sql = " INSERT INTO GSAT_ADMIT "
				   + " (ID,SEO,TITLE, CONTENT,CREATE_BY,CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				   + " VALUES(?, ?, ?, ?, ?, now(), ?, now()) ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATAdmit.getId(),gSATAdmit.getSeo(),
				gSATAdmit.getTitle(),gSATAdmit.getContent(),
				gSATAdmit.getCreate_by(), gSATAdmit.getUpdate_by()});
		
	}
	
	@Override
	public void update(GSATAdmit gSATAdmit) {
//	  sort(gSATAdmit);
		String sql = " UPDATE GSAT_ADMIT SET TITLE = ?,SEO = ?,"
				   + " CONTENT = ?,UPDATE_BY = ?,"
				   + " UPDATE_DATE = now() WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { 
				gSATAdmit.getTitle(),gSATAdmit.getSeo(),
				gSATAdmit.getContent(),gSATAdmit.getUpdate_by(), gSATAdmit.getId() });
		
	}
	
	@Override
	public void updateClickRate(GSATAdmit gSATAdmit) {
		
		String sql = " UPDATE GSAT_ADMIT SET CLICK_RATE = CLICK_RATE+1 WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATAdmit.getId() });
		
	}
	
	@Override
	public void delete(GSATAdmit gSATAdmit ,Integer id) {
//	  deleteSort(gSATAdmit);
		String sql = " DELETE FROM GSAT_ADMIT WHERE ID = ? ";
		
		postgreJdbcTemplate.update(sql, new Object[] { id });
		
	}	
	
	public void addSort(GSATAdmit gSATAdmit){
        String sql = "UPDATE GSAT_ADMIT SET SORT = SORT +1 WHERE SORT >= ? ";
        postgreJdbcTemplate.update(sql, new Object[]{ gSATAdmit.getSort()});
    }
    
    public void sort(GSATAdmit gSATAdmit) {
        String sql = "SELECT COUNT(*) FROM GSAT_ADMIT WHERE SORT = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSATAdmit.getSort()});

        //若原有排序號碼已存在
        if (count > 0) {
            sql = "SELECT SORT FROM GSAT_ADMIT WHERE ID = ?";
            
            GSATWinner oldGSATWinner = postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),new Object[] { gSATAdmit.getId()});
            
            if (gSATAdmit.getSort() < oldGSATWinner.getSort()) {
                
                //將原有排序號碼與新排序號碼中間之號碼全部+1
                sql = "UPDATE GSAT_ADMIT SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ?";
                postgreJdbcTemplate.update(sql, new Object[]{ oldGSATWinner.getSort(), gSATAdmit.getSort()});
            } else if (gSATAdmit.getSort() > oldGSATWinner.getSort()){
                
                //將原有排序號碼與新排序號碼中間之號碼全部-1
                sql = "UPDATE GSAT_ADMIT SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ?";
                postgreJdbcTemplate.update(sql, new Object[]{ gSATAdmit.getSort(), oldGSATWinner.getSort()});
            }           
        }
        sql = "UPDATE GSAT_ADMIT SET SORT = ? WHERE ID = ?";
        postgreJdbcTemplate.update(sql, new Object[]{gSATAdmit.getSort(), gSATAdmit.getId()});
    } 
    
    public void deleteSort(GSATAdmit gSATAdmit){
        String sql1 = "SELECT SORT FROM GSAT_WINNER WHERE ID = ?";
        
        GSATWinner oldGSATWinner = postgreJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<GSATWinner>(GSATWinner.class),new Object[] { gSATAdmit.getId()});

        String sql = "UPDATE GSAT_WINNER SET SORT = SORT -1 WHERE SORT > ?";

        postgreJdbcTemplate.update(sql, new Object[]{ oldGSATWinner.getSort()});
    }   
}
