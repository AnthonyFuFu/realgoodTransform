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

import com.tkb.realgoodTransform.dao.MarqueeDao;
import com.tkb.realgoodTransform.model.Marquee;



@Repository
public class MarqueeDaoImpl implements MarqueeDao {

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
	public List<Marquee> getList(int pageCount, int pageStart, Marquee marquee) {
		List<Object> args = new ArrayList<Object>();
		String sql ="SELECT * FROM MARQUEE ";
		if(marquee.getTitle() != null && !"".equals(marquee.getTitle())) {
			sql += " WHERE TITLE LIKE ? ";
			args.add("%" + marquee.getTitle() + "%");
		}
		sql += " ORDER BY SORT LIMIT ? OFFSET ?";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Marquee>(Marquee.class),args.toArray());
	}

	@Override
	public Integer getCount(Marquee marquee) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM MARQUEE";
		if(marquee.getTitle() != null && !"".equals(marquee.getTitle())) {
			sql += " WHERE TITLE LIKE ? ";
			args.add("%" + marquee.getTitle() + "%");
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public Marquee getData(Marquee marquee) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM MARQUEE WHERE ID = ? ";
		args.add(marquee.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Marquee>(Marquee.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM MARQUEE ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class)+1;
		}catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(Marquee marquee) {
		addSort(marquee);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(marquee);
		String sql = "INSERT INTO MARQUEE (ID, TITLE, CONTENT, LINK, SORT, BEGIN_DATE, END_DATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, CLICK_RATE) "
				   +" VALUES (:id, :title, :content, :link, :sort, :begin_date, :end_date, :create_by, now(), :update_by, now(),0)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter,  keyHolder);
 
	}
	public void addSort(Marquee marquee) {
		String sql = "UPDATE MARQUEE SET SORT = SORT + 1 WHERE SORT >= ?";
		postgresqlJdbcTemplate.update(sql, new Object[] {marquee.getSort()});
	}

	@Override
	public void update(Marquee marquee) {
		sort(marquee);
		List<Object> args = new ArrayList<Object>();
		String sql = "UPDATE MARQUEE SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ";
		args.add(marquee.getUpdate_by());
		if(marquee.getTitle() != null) {
			sql += "TITLE = ?, ";
			args.add(marquee.getTitle());
		}
		if(marquee.getContent() != null) {
			sql += "CONTENT = ?, ";
			args.add(marquee.getContent());
		}
		if(marquee.getLink() != null) {
			sql += "LINK = ?, ";
			args.add(marquee.getLink());
		}
		if(marquee.getSort() != null) {
			sql += "SORT = ?, ";
			args.add(marquee.getSort());
		}
		if(marquee.getBegin_date() != null) {
			sql += "BEGIN_DATE = ?, ";
			args.add(marquee.getBegin_date());
		}
		if(marquee.getEnd_date() != null) {
			sql += "END_DATE = ? ";
			args.add(marquee.getEnd_date());
		}
		sql += "WHERE ID = ?";
		args.add(marquee.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}
	public void sort(Marquee marquee) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM MARQUEE WHERE SORT = ? ";
		args.add(marquee.getSort());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		//若原有排序號碼已存在
		if(count>0) {
			sql ="SELECT SORT FROM MARQUEE WHERE ID = ? ";
			Marquee oldMarquee = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Marquee>(Marquee.class),new Object[] {marquee.getId()});
			if(marquee.getSort()<oldMarquee.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE MARQUEE SET SORT = SORT + 1 WHERE ? >=SORT AND SORT >= ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {oldMarquee.getSort(), marquee.getSort()});
			}else if(marquee.getSort()>oldMarquee.getSort()){
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE MARQUEE SET SORT = SORT - 1 WHERE ? >=SORT AND SORT >= ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {marquee.getSort(), oldMarquee.getSort()});
			}
		}
		sql = "UPDATE MARQUEE SET SORT = ? WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {marquee.getSort(), marquee.getId()});
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM MARQUEE WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}

	@Override
	public void resetSort() {
		String sql = "SELECT COUNT(*) FROM MARQUEE ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class);
		sql = "SELECT SORT FROM MARQUEE ORDER BY SORT ASC ";
		List<Integer> sortList = postgresqlJdbcTemplate.queryForList(sql, Integer.class);
		for(int i=0;i<count;i++) {
			String sql2 = "UPDATE MARQUEE SET SORT = ? WHERE SORT = ? ";
			postgresqlJdbcTemplate.update(sql2, new Object[] {i+1, sortList.get(i)});
		}

	}

	@Override
	public List<Marquee> getFrontList(Marquee marquee) {
			List<Object> args = new ArrayList<Object>();
			String sql = " SELECT * FROM MARQUEE WHERE "
					+ " TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
					+ " AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "	;
	
			sql += " ORDER BY SORT ";
			return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Marquee>(Marquee.class),args.toArray());
			
		}
	
	
	
	@Override
	public void updateClickRate(Marquee marquee) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE MARQUEE SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(marquee.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());
	}

	@Override
	public List<Map<String, Object>> getNormalBannerList() {
		String sql = "select * from marquee";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(Marquee marquee) {
		String sql = " INSERT into marquee (id,title,content,link,click_rate,begin_date,end_date,create_by,create_date,update_by,update_date,sort) VALUES (:id,:title,:content,:link,:click_rate,:begin_date,:end_date,:create_by,:create_date,:update_by,:update_date,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET title = :title, content = :content, link = :link, click_rate = :click_rate, begin_date = :begin_date, end_date = :end_date, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, sort = :sort"; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(marquee);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}

}
 