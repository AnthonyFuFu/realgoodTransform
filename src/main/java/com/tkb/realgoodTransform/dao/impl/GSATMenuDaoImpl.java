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

import com.tkb.realgoodTransform.dao.GSATMenuDao;
import com.tkb.realgoodTransform.model.GSATMenu;


/**
 * 類別Dao實作類
 */
@Repository
public class GSATMenuDaoImpl  implements GSATMenuDao{
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgreJdbcTemplate;
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	@Autowired
	@Qualifier("fifthJdbcTemplate")
	private JdbcTemplate oracJdbcTemplate;
	
	
	
    //抓取類別階層清單	
	@Override
	public List<GSATMenu> getSubList(GSATMenu gSATMenu) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_MENU WHERE PARENT_ID = ? ";
		
		args.add(gSATMenu.getParent_id());
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),args.toArray());
	}
	
	@Override
	public List<GSATMenu> getLayerList(String layer, GSATMenu gSATMenu) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_MENU WHERE PARENT_ID = ? AND LAYER = ?  ORDER BY SORT";
		
		args.add(gSATMenu.getParent_id());
		args.add(layer);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),args.toArray());
		
	}
	
	@Override
	public List<GSATMenu> getList(GSATMenu gSATMenu) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM GSAT_MENU WHERE LAYER = ? AND SHOW='1' ";			

		if(gSATMenu.getLayer() == null) {
			gSATMenu.setLayer("1");
		}
		
		args.add(gSATMenu.getLayer());
		
		if(gSATMenu.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(gSATMenu.getParent_id());
		}	
		
		if(gSATMenu.getName() != null && !"".equals(gSATMenu.getName())) {
			sql += " AND NAME = ? ";
			args.add(gSATMenu.getName());
		}		

		sql += " ORDER BY SORT ";

		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),args.toArray());
		
	}
	
	//後台清單頁功能	
	@Override
	public List<GSATMenu> getList(int pageCount, int pageStart,GSATMenu gSATMenu) {
        
        List<Object> args = new ArrayList<Object>();
        
        String sql = " SELECT * FROM GSAT_MENU WHERE LAYER = ? ";         

        if(gSATMenu.getLayer() == null) {
        	gSATMenu.setLayer("1");
        }
        
        args.add(gSATMenu.getLayer());
        
        if(gSATMenu.getParent_id() != null) {
            sql += " AND PARENT_ID = ? ";
            args.add(gSATMenu.getParent_id());
        }   
        
        if(gSATMenu.getName() != null && !"".equals(gSATMenu.getName())) {
            sql += " AND NAME LIKE ? ";
            args.add("%"+gSATMenu.getName()+"%");
        }       

        sql += " ORDER BY SORT LIMIT ? OFFSET ? ";
        
        args.add(pageCount);
        args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),args.toArray());
        
    }
	
	//計算清單數量	
	@Override
	public Integer getCount(GSATMenu gSATMenu) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) FROM GSAT_MENU WHERE LAYER = ? ";		

		if(gSATMenu.getLayer() == null) {
			gSATMenu.setLayer("1");
		}
		
		args.add(gSATMenu.getLayer());
		
		if(gSATMenu.getParent_id() != null) {
			sql += " AND PARENT_ID = ? ";
			args.add(gSATMenu.getParent_id());
		}			
		
		if(gSATMenu.getName() != null && !"".equals(gSATMenu.getName())) {
			sql += " AND NAME LIKE ? ";
			args.add("%"+gSATMenu.getName()+"%");
		}		
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
		
	}
	
	//抓取該ID資料	
	@Override
	public GSATMenu getData(GSATMenu gSATMenu) {
		
		String sql = " SELECT * FROM GSAT_MENU WHERE ID = ? ";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),new Object[] { gSATMenu.getId() });
	}
	
	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT_MENU ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}
	
	//新增功能	
	@Override
	public void add(GSATMenu gSATMenu) {
		addSort(gSATMenu);
		String sql = " INSERT INTO GSAT_MENU(ID, PARENT_ID, NAME, LAYER,LINK, SORT,SHOW, CREATE_BY, CREATE_DATE,"
				   + " UPDATE_BY, UPDATE_DATE)VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now()) ";
		
		postgreJdbcTemplate.update(sql, new Object[] { gSATMenu.getId(), gSATMenu.getParent_id(),
				gSATMenu.getName(), gSATMenu.getLayer(),gSATMenu.getLink(),gSATMenu.getSort(),gSATMenu.getShow(), gSATMenu.getCreate_by(), gSATMenu.getUpdate_by() });
		
	}
	
	//修改功能	
	@Override
	public void update(GSATMenu gSATMenu) {
		sort(gSATMenu);
		String sql = " UPDATE GSAT_MENU SET NAME = ?, LINK = ?,SORT = ?,SHOW = ? ,UPDATE_BY = ?, UPDATE_DATE = now()"
				   + " WHERE ID = ? ";
		postgreJdbcTemplate.update(sql, new Object[] { gSATMenu.getName(),gSATMenu.getLink(),gSATMenu.getSort(),gSATMenu.getShow(),
				gSATMenu.getUpdate_by(), gSATMenu.getId() });
		
	}
	
	public void addSort(GSATMenu gSATMenu){
		String sql = "UPDATE GSAT_MENU SET SORT = SORT +1 WHERE SORT >= ? AND PARENT_ID = ?";
		postgreJdbcTemplate.update(sql, new Object[]{ gSATMenu.getSort(), gSATMenu.getParent_id() });
	}
	
	public void sort(GSATMenu gSATMenu) {
		String sql = "SELECT COUNT(*) FROM GSAT_MENU WHERE SORT = ? AND PARENT_ID = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSATMenu.getSort(), gSATMenu.getParent_id()});
		//若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT SORT FROM GSAT_MENU WHERE ID = ? AND PARENT_ID = ?";
			
			GSATMenu oldGSATMenu = postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),new Object[] { gSATMenu.getId(), gSATMenu.getParent_id()  });
					
			if (gSATMenu.getSort() < oldGSATMenu.getSort()) {
				
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE GSAT_MENU SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ?";
				postgreJdbcTemplate.update(sql, new Object[]{ oldGSATMenu.getSort(), gSATMenu.getSort(), gSATMenu.getParent_id() });
			} else if (gSATMenu.getSort() > oldGSATMenu.getSort()){
				
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE GSAT_MENU SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ? AND PARENT_ID = ?";
				postgreJdbcTemplate.update(sql, new Object[]{ gSATMenu.getSort(), oldGSATMenu.getSort(), gSATMenu.getParent_id()});
			}			
		}
		sql = "UPDATE GSAT_MENU SET SORT = ? WHERE ID = ? AND PARENT_ID = ?";
		postgreJdbcTemplate.update(sql, new Object[]{gSATMenu.getSort(), gSATMenu.getId(), gSATMenu.getParent_id()});
	}
	
	public void updateEditor(GSATMenu gSATMenu) {
		String sql = "UPDATE GSAT_MENU SET UPDATE_BY = ?, UPDATE_DATE = now()";
		postgreJdbcTemplate.update(sql, new Object[] { gSATMenu.getUpdate_by() });
	}
	
	@Override
	public void resetSort(GSATMenu gSATMenu){
		gSATMenu.setParent_id(0);
		String sql = "SELECT COUNT(*) FROM GSAT_MENU WHERE PARENT_ID = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSATMenu.getParent_id()});
		int parent_id = gSATMenu.getParent_id();
		sql = "SELECT SORT FROM GSAT_MENU WHERE PARENT_ID = ? ORDER BY SORT ASC";
		List<Integer>sortList = postgreJdbcTemplate.queryForList(sql,Integer.class, new Object[] { parent_id });
		
		for(int i=0; i<count; i++){
			String sql2 = "UPDATE GSAT_MENU SET SORT = ? WHERE SORT = ? AND PARENT_ID = "+parent_id;
			postgreJdbcTemplate.update(sql2, new Object[] {i+1, sortList.get(i)});
		}
	}
	
	public void deleteSort(GSATMenu gSATMenu){
		gSATMenu.setParent_id(0);
		String sql1 = "SELECT SORT FROM GSAT_MENU WHERE ID = ?";
		
		GSATMenu oldGSATMenu =  postgreJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),new Object[] {gSATMenu.getId()});
		
		String sql = "UPDATE GSAT_MENU SET SORT = SORT -1 WHERE SORT > ? AND PARENT_ID = ?";

		postgreJdbcTemplate.update(sql, new Object[]{ oldGSATMenu.getSort(), gSATMenu.getParent_id() });
	}
	
	//刪除功能	
	@Override
	public void delete(GSATMenu gSATMenu ,Integer id) {		
		deleteSort(gSATMenu);
		String sql = " DELETE FROM GSAT_MENU WHERE ID = ? ";
		postgreJdbcTemplate.update(sql, new Object[] { id });		
	}
	
	@Override
	public String checkGSATMenu(GSATMenu gSATMenu) {
		String sql = "SELECT * FROM GSAT_MENU WHERE NAME = ? AND LAYER = ?";
		List<GSATMenu> list = postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSATMenu>(GSATMenu.class),new Object[]{gSATMenu.getName(),gSATMenu.getLayer()});
				
		return list.size() == 0 ? null : list.get(0).getName();
	}	
	
	@Override
	public Integer getCategoryId(String name) {
		String sql = " SELECT ID FROM GSAT_MENU WHERE NAME = ? ";
		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{name});
		
	}	
	
	
	
//	===========================
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from gsat_menu";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}
	@Override
	public void updateNormalData(GSATMenu gSATMenu) {
		String sql = " INSERT into gsat_menu (id,parent_id,name,layer,create_by,create_date,update_by,update_date,show,sort,link) VALUES (:id,:parent_id,:name,:layer,:create_by,:create_date,:update_by,:update_date,:show,:sort,:link)  "
					+ " ON CONFLICT(id) "
					+ " DO UPDATE SET parent_id = :parent_id, name = :name, layer = :layer, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, show = :show, sort = :sort, link = :link "; 
		
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(gSATMenu);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}

	
}
