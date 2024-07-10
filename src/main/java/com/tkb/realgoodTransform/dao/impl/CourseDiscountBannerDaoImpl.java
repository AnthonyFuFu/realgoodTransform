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

import com.tkb.realgoodTransform.dao.CourseDiscountBannerDao;
import com.tkb.realgoodTransform.model.Banner;
import com.tkb.realgoodTransform.model.CourseDiscountBanner;



@Repository
public class CourseDiscountBannerDaoImpl implements CourseDiscountBannerDao {

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
	public List<CourseDiscountBanner> getList(int pageCount, int pageStart, CourseDiscountBanner courseDiscountBanner) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM COURSE_DISCOUNT_BANNER  ";

		if (courseDiscountBanner.getTitle() != null && !"".equals(courseDiscountBanner.getTitle())) {
			sql += " WHERE TITLE LIKE ? ";
			args.add("%" + courseDiscountBanner.getTitle() + "%");
		}

		sql += " ORDER BY SORT LIMIT ? OFFSET ?";

		args.add(pageCount);
		args.add(pageStart);

		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<CourseDiscountBanner>(CourseDiscountBanner.class), args.toArray());
	}

	@Override
	public List<CourseDiscountBanner> getFrontList(CourseDiscountBanner courseDiscountBanner) {
		String sql = "SELECT * FROM COURSE_DISCOUNT_BANNER WHERE SHOW = 1 "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE ";
		sql += "ORDER BY SORT ";
		return postgresqlJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<CourseDiscountBanner>(CourseDiscountBanner.class));
	}

	@Override
	public Integer getCount(CourseDiscountBanner courseDiscountBanner) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM COURSE_DISCOUNT_BANNER ";

		if (courseDiscountBanner.getTitle() != null && !"".equals(courseDiscountBanner.getTitle())) {
			sql += " WHERE TITLE LIKE ? ";
			args.add("%" + courseDiscountBanner.getTitle() + "%");
		}

		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public CourseDiscountBanner getData(CourseDiscountBanner courseDiscountBanner) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM COURSE_DISCOUNT_BANNER WHERE ID = ? ";
		args.add(courseDiscountBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<CourseDiscountBanner>(CourseDiscountBanner.class), args.toArray());
	}

	@Override
	public CourseDiscountBanner getFrontData(CourseDiscountBanner courseDiscountBanner) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM COURSE_DISCOUNT_BANNER WHERE ID = ? ";
		args.add(courseDiscountBanner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<CourseDiscountBanner>(CourseDiscountBanner.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM COURSE_DISCOUNT_BANNER ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(CourseDiscountBanner courseDiscountBanner) {
		addSort(courseDiscountBanner);
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(courseDiscountBanner);
		String sql = "INSERT INTO COURSE_DISCOUNT_BANNER(ID, IMAGE, TITLE, LINK, CLICK_RATE, SHOW, SORT, BEGIN_DATE, END_DATE, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE) "
				+ " VALUES(:id, :image, :title, :link, 0, :show, :sort, :begin_date, :end_date, :create_by, now(), :update_by, now())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	public void addSort(CourseDiscountBanner courseDiscountBanner) {
		List<Object> args = new ArrayList<>();
		String sql = "UPDATE COURSE_DISCOUNT_BANNER SET SORT = SORT + 1 WHERE SORT >= ? ";
		args.add(courseDiscountBanner.getSort());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void update(CourseDiscountBanner courseDiscountBanner) {
		updateSort(courseDiscountBanner);
		List<Object> args = new ArrayList<>();
		String sql = "UPDATE COURSE_DISCOUNT_BANNER SET UPDATE_BY = ?, UPDATE_DATE = NOW(), ";
		args.add(courseDiscountBanner.getUpdate_by());
		if (courseDiscountBanner.getImage() != null) {
			sql += " IMAGE = ?,";
			args.add(courseDiscountBanner.getImage());
		}
		if (courseDiscountBanner.getTitle() != null) {
			sql += " TITLE = ?,";
			args.add(courseDiscountBanner.getTitle());
		}
		if (courseDiscountBanner.getLink() != null) {
			sql += " LINK = ?,";
			args.add(courseDiscountBanner.getLink());
		}
		if (courseDiscountBanner.getShow() != null) {
			sql += " SHOW = ?,";
			args.add(courseDiscountBanner.getShow());
		}
		if (courseDiscountBanner.getBegin_date() != null) {
			sql += " BEGIN_DATE = ?,";
			args.add(courseDiscountBanner.getBegin_date());
		}
		if (courseDiscountBanner.getEnd_date() != null) {
			sql += " END_DATE = ?,";
			args.add(courseDiscountBanner.getEnd_date());
		}
		if (courseDiscountBanner.getSort() != null) {
			sql += " SORT = ?";
			args.add(courseDiscountBanner.getSort());
		}
		sql += " WHERE ID = ?";
		args.add(courseDiscountBanner.getId());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM COURSE_DISCOUNT_BANNER WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });

	}

	@Override
	public void resetSort() {
//		List<Object> args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT_BANNER ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class);

		sql = "SELECT SORT FROM COURSE_DISCOUNT_BANNER ORDER BY SORT ASC ";

		for (int i = 0; i < count; i++) {
//			CourseDiscountBanner sortBanner = (CourseDiscountBanner)postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<CourseDiscountBanner>(CourseDiscountBanner.class)).get(i);
			List<Integer> sortList = postgresqlJdbcTemplate.queryForList(sql, Integer.class);
			String sql2 = "UPDATE COURSE_DISCOUNT_BANNER SET SORT = ? WHERE SORT = ? ";
//			args.add(sortBanner.getSort());
//			args.add(sortList.get(i));
			postgresqlJdbcTemplate.update(sql2, new Object[] { i + 1, sortList.get(i) });
		}

	}

	@Override
	public Integer getFrontCount(CourseDiscountBanner courseDiscountBanner) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void updateClickRate(CourseDiscountBanner courseDiscountBanner) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE COURSE_DISCOUNT_BANNER SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(courseDiscountBanner.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());
		
	}
	
	@Override
	public void updateSort(CourseDiscountBanner courseDiscountBanner) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT_BANNER WHERE SORT = ? ";
		args.add(courseDiscountBanner.getSort());
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
		//若原有排序號碼已存在
		if(count>0) {
			sql = "SELECT SORT FROM COURSE_DISCOUNT_BANNER WHERE ID = ? ";
			Banner oldBanner = postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Banner>(Banner.class), new Object[] {courseDiscountBanner.getId()});
			if(courseDiscountBanner.getSort()<oldBanner.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE COURSE_DISCOUNT_BANNER SET SORT = SORT + 1 WHERE ? >= SORT AND SORT >= ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {oldBanner.getSort(), courseDiscountBanner.getSort()});
			}else if(courseDiscountBanner.getSort()>oldBanner.getSort()) {
				//將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE COURSE_DISCOUNT_BANNER SET SORT = SORT - 1 WHERE ? >= SORT AND SORT >= ? ";
				postgresqlJdbcTemplate.update(sql, new Object[] {courseDiscountBanner.getSort(), oldBanner.getSort()});
			}
		}
		sql = "UPDATE COURSE_DISCOUNT_BANNER SET SORT = ? WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {courseDiscountBanner.getSort(), courseDiscountBanner.getId()});
	}
	
	
//	===================
	

	@Override
	public List<Map<String, Object>> getNormalCourseBannerList() {
		String sql = "select * from course_discount_banner";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(CourseDiscountBanner courseDiscountBanner) {
		String sql = " INSERT into course_discount_banner (id,image,title,link,click_rate,begin_date,end_date,create_by,create_date,update_by,update_date,show,sort) VALUES (:id,:image,:title,:link,:click_rate,:begin_date,:end_date,:create_by,:create_date,:update_by,:update_date,:show,:sort)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET image = :image, title = :title, link = :link, click_rate = :click_rate, begin_date = :begin_date, end_date = :end_date, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, show = :show, sort = :sort "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(courseDiscountBanner);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}


}
