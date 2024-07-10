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

import com.tkb.realgoodTransform.dao.BannerDao;
import com.tkb.realgoodTransform.model.Banner;



@Repository
public class BannerDaoImpl implements BannerDao {
	
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
	public List<Banner> getList(int pageCount, int pageStart, Banner banner) {
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM BANNER  ";
		
		if(banner.getTitle() != null && !"".equals(banner.getTitle())) {
			sql += " WHERE TITLE LIKE ? ";
			args.add("%" + banner.getTitle() + "%");
		}
		sql += " ORDER BY SORT LIMIT ? OFFSET ? ";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Banner>(Banner.class), args.toArray());
	}

	@Override
	public List<Banner> getFrontList(Banner banner) {
		StringBuffer sql = new StringBuffer("SELECT * FROM BANNER WHERE SHOW = 1 "
				         + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') >= BEGIN_DATE "
				         + "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'),'yyyy-MM-dd') <= END_DATE ");
		sql.append("ORDER BY SORT ");
		return postgresqlJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<Banner>(Banner.class));
	}

	@Override
	public Integer getCount(Banner banner) {
		List<Object> args = new ArrayList<Object>();
		
		String sql = "SELECT COUNT(*) FROM BANNER ";
		
		if(banner.getTitle() != null && !"".equals(banner.getTitle())) {
			sql += " WHERE TITLE LIKE ?";
			args.add("%" +banner.getTitle()+ "%");
		}
		
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,args.toArray());
	}


	@Override
	public Banner getData(Banner banner) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM BANNER WHERE ID = ?";
		args.add(banner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Banner>(Banner.class),args.toArray());
	}

	@Override
	public Banner getFrontData(Banner banner) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM BANNER WHERE ID = ?";
		args.add(banner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Banner>(Banner.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql ="SELECT ID FROM BANNER ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class)+1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(Banner banner) {
		addSort(banner);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(banner);
		String sql = "INSERT INTO BANNER(ID, IMAGE, IMAGE_RWD, TITLE, LINK, CLICK_RATE, SHOW, SORT, BEGIN_DATE, END_DATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)"
				   + " VALUES(:id, :image, :image_rwd, :title, :link, 0, :show, :sort, :begin_date, :end_date, :create_by, now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}
	
	public void addSort(Banner banner) {
		List<Object> args = new ArrayList<>();
		String sql ="UPDATE BANNER SET SORT = SORT + 1 WHERE SORT >= ? ";
		args.add(banner.getSort());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void update(Banner banner) {
		sort(banner);
		List<Object> args = new ArrayList<Object>();
		String sql = "UPDATE BANNER SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ";
		args.add(banner.getUpdate_by());
		if(banner.getImage() != null) {
			sql += "IMAGE = ?, ";
			args.add(banner.getImage());
		}
		if(banner.getImage_rwd() != null) {
			sql += "IMAGE_RWD = ?, ";
			args.add(banner.getImage_rwd());
		}
		if(banner.getTitle() != null) {
			sql += "TITLE = ?, ";
			args.add(banner.getTitle());
		}
		if(banner.getLink() != null) {
			sql += "LINK = ?, ";
			args.add(banner.getLink());
		}
		if(banner.getShow() != null) {
			sql += "SHOW = ?, ";
			args.add(banner.getShow());
		}
		if(banner.getSort() != null) {
			sql += "SORT = ?, ";
			args.add(banner.getSort());
		}
		if(banner.getBegin_date() != null) {
			sql += "BEGIN_DATE = ?, ";
			args.add(banner.getBegin_date());
		}
		if(banner.getEnd_date() != null) {
			sql += "END_DATE = ? ";
			args.add(banner.getEnd_date());
		}
		sql += "WHERE ID = ? ";
		args.add(banner.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}
	
	public void sort(Banner banner) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM BANNER WHERE SORT = ? ";
		args.add(banner.getSort());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		//若原有排序號碼已存在
		if(count>0) {
			sql = "SELECT SORT FROM BANNER WHERE ID = ? ";
			Banner oldBanner = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Banner>(Banner.class), new Object[] {banner.getId()});
			if(banner.getSort()<oldBanner.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE BANNER SET SORT = SORT + 1 WHERE ? >= SORT AND SORT >= ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {oldBanner.getSort(), banner.getSort()});
			}else if(banner.getSort()>oldBanner.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE BANNER SET SORT = SORT - 1 WHERE ? >= SORT AND SORT >= ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {banner.getSort(), oldBanner.getSort()});
			}
		}
		sql = "UPDATE BANNER SET SORT = ? WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {banner.getSort(), banner.getId()});
	}


	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM BANNER WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});
	}

	@Override
	public void resetSort() {
		String sql = "SELECT COUNT(*) FROM BANNER ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class);
		sql = "SELECT SORT FROM BANNER ORDER BY SORT ASC";
		List<Integer> sortList = postgresqlJdbcTemplate.queryForList(sql, Integer.class);
		for(int i=0; i<count; i++) {
			String sql2 = "UPDATE BANNER SET SORT = ? WHERE SORT = ? ";
			postgresqlJdbcTemplate.update(sql2, new Object[] {i+1, sortList.get(i)});
			
		}

	}

	
	@Override
	public void updateClickRate(Banner banner) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE BANNER SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(banner.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());
	}
	
	
//	===========================
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from banner";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}
	@Override
	public void updateNormalData(Banner banner) {
		String sql = " INSERT into banner (id,image,title,link,click_rate,begin_date,end_date,create_by,create_date,update_by,update_date,show,sort,image_rwd) VALUES (:id,:image,:title,:link,:click_rate,:begin_date,:end_date,:create_by,:create_date,:update_by,:update_date,:show,:sort,:image_rwd)  "
					+ " ON CONFLICT(id) "
					+ " DO UPDATE SET image = :image, title = :title, link = :link, click_rate = :click_rate, begin_date = :begin_date, end_date = :end_date, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, show = :show, sort = :sort, image_rwd = :image_rwd "; 
		
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(banner);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}



}
