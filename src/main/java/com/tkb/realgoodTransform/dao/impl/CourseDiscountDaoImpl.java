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

import com.tkb.realgoodTransform.dao.CourseDiscountDao;
import com.tkb.realgoodTransform.model.CourseDiscount;



@Repository
public class CourseDiscountDaoImpl implements CourseDiscountDao {

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
	public List<CourseDiscount> getList(int pageCount, int pageStart, CourseDiscount courseDiscount) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT CD.*, C.NAME AS CATEGORY_NAME " + "FROM COURSE_DISCOUNT CD "
				+ "LEFT JOIN COURSE_DISCOUNT_CATEGORY C ON C.ID = CAST(CD.CATEGORY AS INTEGER) ";
		if (courseDiscount.getTitle() != null && !"".equals(courseDiscount.getTitle())) {
			sql += "WHERE TITLE LIKE ? ";
			args.add("%" + courseDiscount.getTitle() + "%");
		}
		sql += "ORDER BY CD.COURSE_DISCOUNT_TOP DESC, CD.UPDATE_DATE DESC LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<CourseDiscount>(CourseDiscount.class),
				args.toArray());
	}

	@Override
	public List<CourseDiscount> getFrontList(int pageCount, int pageStart, CourseDiscount courseDiscount,
			String search_sort) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT CD.*, C.NAME AS CATEGORY_NAME FROM COURSE_DISCOUNT CD "
				+ "LEFT JOIN COURSE_DISCOUNT_CATEGORY C ON C.ID = CAST(CD.CATEGORY AS INTEGER) WHERE SHOW = 1 "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE ";
		if (courseDiscount.getCategory() != null && !"".equals(courseDiscount.getCategory())) {
			sql += "AND CD.CATEGORY = ? ";
			args.add(courseDiscount.getCategory());
		}
		if ("week".equals(courseDiscount.getDate())) {

			sql += " AND( " + " (BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
					+ " AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) " + " OR "
					+ " (BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') " + " AND BEGIN_DATE < TO_DATE(?, 'yyyy-MM-dd') "
					+ " AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) " + " ) ";
			args.add(courseDiscount.getWeek_begin_date());
			args.add(courseDiscount.getWeek_begin_date());
			args.add(courseDiscount.getWeek_begin_date());
			args.add(courseDiscount.getWeek_end_date());
			args.add(courseDiscount.getWeek_begin_date());

		} else if ("month".equals(courseDiscount.getDate())) {
			sql += "AND " + "( " + "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) " + "OR "
					+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) " + ") ";
		}
		if ("".equals(search_sort) || search_sort == null || "new".equals(search_sort)) {
			sql += " ORDER BY CD.CREATE_DATE DESC ";
		} else {
			sql += " ORDER BY CD.CLICK_RATE DESC ";
		}
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<CourseDiscount>(CourseDiscount.class),
				args.toArray());
	}

	@Override
	public List<CourseDiscount> getFrontList() {
		String sql = "SELECT CD.*, CDC.NAME AS CATEGORY_NAME " + "FROM COURSE_DISCOUNT CD "
				+ "LEFT JOIN COURSE_DISCOUNT_CATEGORY CDC ON CAST(CD.CATEGORY AS INTEGER) = CDC.ID "
				+ "WHERE COURSE_DISCOUNT_TOP = '1' AND SHOW = '1' "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE "
				+ "ORDER BY CD.UPDATE_DATE DESC ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<CourseDiscount>(CourseDiscount.class));
	}

	@Override
	public Integer getCount(CourseDiscount courseDiscount) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM COURSE_DISCOUNT ");

		if (courseDiscount.getTitle() != null && !"".equals(courseDiscount.getTitle())) {
			sql.append("WHERE TITLE LIKE ? ");
			args.add("%" + courseDiscount.getTitle() + "%");
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public Integer getFrontCount(CourseDiscount courseDiscount) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT CD  " + "WHERE SHOW = 1 "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') >= BEGIN_DATE "
				+ "AND TO_DATE(TO_CHAR(NOW(), 'yyyy-MM-dd'), 'yyyy-MM-dd') <= END_DATE ";

		if (courseDiscount.getCategory() != null && !"".equals(courseDiscount.getCategory())) {
			sql += "AND CD.CATEGORY = ? ";
			args.add(courseDiscount.getCategory());
		}
		if ("week".equals(courseDiscount.getDate())) {

			sql += " AND( " + " (BEGIN_DATE <= TO_DATE(?, 'yyyy-MM-dd') "
					+ " AND END_DATE >= TO_DATE(?, 'yyyy-MM-dd')) " + " OR "
					+ " (BEGIN_DATE > TO_DATE(?, 'yyyy-MM-dd') " + " AND BEGIN_DATE < TO_DATE(?, 'yyyy-MM-dd') "
					+ " AND END_DATE > TO_DATE(?, 'yyyy-MM-dd')) " + " ) ";
			args.add(courseDiscount.getWeek_begin_date());
			args.add(courseDiscount.getWeek_begin_date());
			args.add(courseDiscount.getWeek_begin_date());
			args.add(courseDiscount.getWeek_end_date());
			args.add(courseDiscount.getWeek_begin_date());

		} else if ("month".equals(courseDiscount.getDate())) {
			sql += "AND " + "( " + "DATE_TRUNC('MONTH',BEGIN_DATE) = DATE_TRUNC('MONTH',NOW()) " + "OR "
					+ "DATE_TRUNC('MONTH',END_DATE) = DATE_TRUNC('MONTH',NOW()) " + ") ";
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public CourseDiscount getData(CourseDiscount courseDiscount) {
		List<Object> args = new ArrayList<>();
		String sql = "SELECT * FROM COURSE_DISCOUNT WHERE ID = ? ";
		args.add(courseDiscount.getId());
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<CourseDiscount>(CourseDiscount.class), args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = "SELECT ID FROM COURSE_DISCOUNT ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(CourseDiscount courseDiscount) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(courseDiscount);
		String sql = "INSERT INTO COURSE_DISCOUNT (ID, TITLE, CATEGORY, IMAGE, TYPE_ICON, TYPE_ICON_TEXT, SHOW, PHOTO, "
				+ "COURSE_DISCOUNT_TOP, INDEX_IMAGE, BEGIN_DATE, END_DATE, CLICK_RATE, CREATE_BY, CREATE_DATE, "
				+ "UPDATE_BY, UPDATE_DATE, CONTENT, EDM_TYPE_ID, EDM_URL, PRODUCT_CATEGORY) VALUES(:id, :title, :category, :image, "
				+ ":type_icon, :type_icon_text, :show, :photo, :course_discount_top, :index_image, :begin_date, :end_date, 0, "
				+ ":create_by, now(), :update_by, now(), :content, :edm_type_id, :edm_url, :product_category ) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);

	}

	public void addIndexSort(CourseDiscount courseDiscount) {
		List<Object> args = new ArrayList<>();
		String sql = "UPDATE COURSE_DISCOUNT SET INDEX_SORT = INDEX_SORT + 1 WHERE INDEX_SORT >= ? ";
		args.add(courseDiscount.getIndex_sort());
		postgresqlJdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void update(CourseDiscount courseDiscount) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(courseDiscount);
		String sql = "UPDATE COURSE_DISCOUNT SET TITLE = :title, CATEGORY = :category, CONTENT = :content,TYPE_ICON = :type_icon, TYPE_ICON_TEXT = :type_icon_text, SHOW = :show, PHOTO = :photo, COURSE_DISCOUNT_TOP = :course_discount_top,"
				+ "INDEX_IMAGE = :index_image, BEGIN_DATE = :begin_date, END_DATE = :end_date, UPDATE_BY = :update_by, UPDATE_DATE = now(), EDM_URL = :edm_url, PRODUCT_CATEGORY = :product_category WHERE ID = :id";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}

	public void index_sort(CourseDiscount courseDiscount) {
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT WHERE INDEX_SORT = ? ";
		int count = postgresqlJdbcTemplate.queryForObject(sql, Integer.class,
				new Object[] { courseDiscount.getIndex_sort() });
		// 若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT INDEX_SORT FROM COURSE_DISCOUNT WHERE ID = ? ";
			CourseDiscount oldCourseDiscount = postgresqlJdbcTemplate.queryForObject(sql,
					new BeanPropertyRowMapper<CourseDiscount>(CourseDiscount.class),
					new Object[] { courseDiscount.getId() });
			if (courseDiscount.getIndex_sort() < oldCourseDiscount.getIndex_sort()) {
				sql = "UPDATE COURSE_DISCOUNT SET INDEX_SORT = INDEX_SORT + 1 WHERE ? >= INDEX_SORT AND INDEX_SORT >= ? ";
				postgresqlJdbcTemplate.update(sql,
						new Object[] { oldCourseDiscount.getIndex_sort(), courseDiscount.getIndex_sort() });
			} else if (courseDiscount.getIndex_sort() > oldCourseDiscount.getIndex_sort()) {
				sql = "UPDATE COURSE_DISCOUNT SET INDEX_SORT = INDEX_SORT - 1 WHERE ? >= INDEX_SORT AND INDEX_SORT >= ? ";
				postgresqlJdbcTemplate.update(sql,
						new Object[] { courseDiscount.getIndex_sort(), oldCourseDiscount.getIndex_sort() });
			}
		}
		sql = "UPDATE COURSE_DISCOUNT SET INDEX_SORT = ? WHERE ID = ?";
		postgresqlJdbcTemplate.update(sql, new Object[] { courseDiscount.getIndex_sort(), courseDiscount.getId() });
	}

	@Override
	public void delete(CourseDiscount courseDiscount, Integer id) {
		String sql = "DELETE FROM COURSE_DISCOUNT WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { id });

	}

	@Override
	public Integer checkTopCount(CourseDiscount courseDiscount) {
		String sql = "SELECT COUNT(*) FROM COURSE_DISCOUNT WHERE CAST(COURSE_DISCOUNT_TOP AS INTEGER) = 1 ";
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class);
	}
	
//	========
	
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from COURSE_DISCOUNT";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(CourseDiscount courseDiscount) {
		String sql = " INSERT into COURSE_DISCOUNT (id,title,area,category,image,begin_date,end_date,click_rate,create_by,create_date,update_by,update_date,content,photo,type_icon,type_icon_text,show,exam_analysis,exam_analysis_link,goodies,gifts,course_discount_top,index_image,edm_type_id,edm_url,product_category) VALUES (:id,:title,:area,:category,:image,:begin_date,:end_date,:click_rate,:create_by,:create_date,:update_by,CAST(:update_date AS TIMESTAMP),:content,:photo,:type_icon,:type_icon_text,:show,:exam_analysis,:exam_analysis_link,:goodies,:gifts,:course_discount_top,:index_image,:edm_type_id,:edm_url,:product_category)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET title = :title,area = :area,category = :category,image = :image,begin_date = :begin_date,end_date = :end_date,click_rate = :click_rate,create_by = :create_by,create_date = :create_date,update_by = :update_by,update_date = CAST(:update_date AS TIMESTAMP),content = :content,photo = :photo,type_icon = :type_icon,type_icon_text = :type_icon_text,show = :show,exam_analysis = :exam_analysis,exam_analysis_link = :exam_analysis_link,goodies = :goodies,gifts = :gifts,course_discount_top = :course_discount_top,index_image = :index_image,edm_type_id = :edm_type_id,edm_url = :edm_url,product_category = :product_category "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(courseDiscount);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}
}
