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

import com.tkb.realgoodTransform.dao.GSATDao;
import com.tkb.realgoodTransform.model.GSAT;

/**
 * 考試資訊Dao實作類
 * 
 * @author Wen
 * @version 創建時間：2016-09-29
 */
@Repository
public class GSATDaoImpl implements GSATDao {
	@Autowired
    @Qualifier("postgresqlJdbcTemplate")
	private JdbcTemplate postgreJdbcTemplate;
	@Autowired
    @Qualifier("postgresqlJdbcTemplate2")
	private NamedParameterJdbcTemplate postgresqlJdbcNameTemplate;
	@Autowired
	@Qualifier("fifthJdbcTemplate")
	private JdbcTemplate oracJdbcTemplate;
	
	@Override
	public List<GSAT> getList(int pageCount, int pageStart, GSAT gSAT) {
		/*
		 * 後台清單頁
		 */
		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT G.*, C.NAME AS CATEGORY_NAME "
				+ " FROM GSAT G "
				+ " LEFT JOIN GSAT_CATEGORY C ON C.ID = CAST(G.CATEGORY AS INTEGER)";

		if (gSAT.getCategory() != null && !"".equals(gSAT.getCategory())) {
			sql += " WHERE E.CATEGORY = ? ";
			args.add(gSAT.getCategory());
		}

		sql += " ORDER BY G.CATEGORY ,G.SORT LIMIT ? OFFSET ?";

		args.add(pageCount);
		args.add(pageStart);
		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSAT>(GSAT.class),args.toArray());
	}

	// 計算清單資料筆數搜尋功能使用
	@Override
	public Integer getCount(GSAT gSAT) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM GSAT ";

		if (gSAT.getCategory() != null && !"".equals(gSAT.getCategory())) {
			sql += " WHERE CATEGORY = ? ";
			args.add(gSAT.getCategory());
		}

		return postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());

	}

	// 前台外特專區頁籤
	@Override
	public List<GSAT> getCategoryList(GSAT gSAT) {

//		String sql = " SELECT * FROM GSAT WHERE CATEGORY = ? AND SHOW='1' ORDER BY SORT";

		String sql = " SELECT * FROM GSAT WHERE 1=1";
		if (gSAT.getCategory_name().equals("外特考試科目")) {

			sql += " AND CATEGORY = ? AND SHOW='1' LIMIT 5";

		} else {
			sql += " AND CATEGORY = ? AND SHOW='1'";
		}
		sql += " ORDER BY SORT";

		return postgreJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GSAT>(GSAT.class),new Object[]{ gSAT.getCategory()});

	}

	/*
	 * 取前台文章資料
	 */
	@Override
	public GSAT getData(GSAT gSAT) {
		String sql = " SELECT G.*, GC.NAME AS CATEGORY_NAME "
				+ " FROM GSAT G "
				+ " LEFT JOIN GSAT_CATEGORY GC ON CAST(G.CATEGORY AS INTEGER) = GC.ID "
				+ " WHERE G.ID = ? ";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSAT>(GSAT.class),new Object[] {gSAT.getId()});
	}

	public GSAT getFrontData(GSAT gSAT) {

		String sql = " SELECT G.*, GC.NAME AS CATEGORY_NAME "
				+ " FROM GSAT G "
				+ " LEFT JOIN GSAT_CATEGORY GC ON CAST(G.CATEGORY AS INTEGER)  = GC.ID "
				+ " WHERE G.ID = ? ";
		return postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSAT>(GSAT.class),new Object[] {gSAT.getId()});

	}

	/*
	 * 取下一個ID
	 */
	@Override
	public Integer getNextId() {
		Integer nextId = null;
		String sql = " SELECT ID FROM GSAT ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgreJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}
	
	@Override
	public void add(GSAT gSAT) {
		addSort(gSAT);
		String sql = " INSERT INTO GSAT(ID, CATEGORY, TITLE, SEO,"
				+ " ARTICLE_NAME, ARTICLE_URL, CONTENT,SORT, SHOW,CREATE_BY, CREATE_DATE,"
				+ " UPDATE_BY, UPDATE_DATE, PRODUCT_CATEGORY) "
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?) ";

		postgreJdbcTemplate.update(sql,
				new Object[] { gSAT.getId(), gSAT.getCategory(), gSAT.getTitle(), gSAT.getSeo(),
						gSAT.getArticle_name(), gSAT.getArticle_url(), gSAT.getContent(),
						gSAT.getSort(), gSAT.getShow(), gSAT.getCreate_by(),
						gSAT.getUpdate_by(), gSAT.getProduct_category() });

	}
	@Override
	public void update(GSAT gSAT) {
		sort(gSAT);
		String sql = " UPDATE GSAT SET CATEGORY = ?, TITLE = ?, SEO = ?, ARTICLE_NAME = ?,"
				+ " SHOW = ?, ARTICLE_URL = ?, CONTENT = ?, SORT = ?, UPDATE_BY = ?,"
				+ " UPDATE_DATE = now(), PRODUCT_CATEGORY = ? WHERE ID = ? ";

		postgreJdbcTemplate.update(sql,
				new Object[] { gSAT.getCategory(), gSAT.getTitle(), gSAT.getSeo(),
						gSAT.getArticle_name(), gSAT.getShow(), gSAT.getArticle_url(),
						gSAT.getContent(), gSAT.getSort(), gSAT.getUpdate_by(),
						gSAT.getProduct_category(), gSAT.getId() });

	}

	/*
	 * 文章瀏覽數未使用
	 */
	@Override
	public void updateClickRate(GSAT gSAT) {

		String sql = " UPDATE GSAT SET CLICK_RATE = CLICK_RATE+1 WHERE ID = ? ";

		postgreJdbcTemplate.update(sql, new Object[] { gSAT.getId() });

	}
	@Override
	public void delete(GSAT gSAT, Integer id) {
		deleteSort(gSAT);
		String sql = " DELETE FROM GSAT WHERE ID = ? ";
		postgreJdbcTemplate.update(sql, new Object[] { id });

	}
	
	public void addSort(GSAT gSAT) {
		String sql = "UPDATE GSAT SET SORT = SORT +1 WHERE SORT >= ? AND CATEGORY = ?";
		postgreJdbcTemplate.update(sql, new Object[] { gSAT.getSort(), gSAT.getCategory() });
	}
	
	public void sort(GSAT gSAT) {
		String sql = "SELECT COUNT(*) FROM GSAT WHERE SORT = ? AND CATEGORY = ?";
		int count = postgreJdbcTemplate.queryForObject(sql.toString(), Integer.class, new Object[]{gSAT.getSort(), gSAT.getCategory()});

		// 若原有排序號碼已存在
		if (count > 0) {
			sql = "SELECT SORT FROM GSAT WHERE ID = ? AND CATEGORY = ?";
			GSAT oldGSAT = postgreJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<GSAT>(GSAT.class),new Object[] { gSAT.getId(), gSAT.getCategory() });
			if (gSAT.getSort() < oldGSAT.getSort()) {

				// 將原有排序號碼與新排序號碼中間之號碼全部+1
				sql = "UPDATE GSAT SET SORT = SORT +1 WHERE ? >= SORT AND SORT >= ? AND CATEGORY = ?";
				postgreJdbcTemplate.update(sql,new Object[] { oldGSAT.getSort(), gSAT.getSort(), gSAT.getCategory() });
			} else if (gSAT.getSort() > oldGSAT.getSort()) {

				// 將原有排序號碼與新排序號碼中間之號碼全部-1
				sql = "UPDATE GSAT SET SORT = SORT -1 WHERE ? >= SORT AND SORT >= ? AND CATEGORY = ?";
				postgreJdbcTemplate.update(sql,new Object[] { gSAT.getSort(), oldGSAT.getSort(), gSAT.getCategory() });
			}
		}
		sql = "UPDATE GSAT SET SORT = ? WHERE ID = ? AND CATEGORY = ?";
		postgreJdbcTemplate.update(sql,new Object[] { gSAT.getSort(), gSAT.getId(), gSAT.getCategory() });
	}

	public void deleteSort(GSAT gSAT) {
		String sql1 = "SELECT SORT FROM GSAT WHERE ID = ?";

		GSAT oldGSAT = postgreJdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<GSAT>(GSAT.class),new Object[] { gSAT.getId()});

		String sql = "UPDATE GSAT SET SORT = SORT -1 WHERE SORT > ? AND CATEGORY = ?";

		postgreJdbcTemplate.update(sql, new Object[] { oldGSAT.getSort(), gSAT.getCategory() });
	}
	
	
	
//	===========================
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from GSAT";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}
	@Override
	public void updateNormalData(GSAT gSAT) {
		String sql = " INSERT into gsat (id,category,title,seo,article_name,article_url,create_by,create_date,update_by,update_date,content,sort,show,product_category) VALUES (:id,:category,:title,:seo,:article_name,:article_url,:create_by,:create_date,:update_by,:update_date,:content,:sort,:show,:product_category)  "
					+ " ON CONFLICT(id) "
					+ " DO UPDATE SET category = :category, title = :title, seo = :seo, article_name = :article_name, article_url = :article_url, create_by = :create_by, create_date = :create_date, update_by = :update_by, update_date = :update_date, content = :content, sort = :sort, show = :show, product_category = :product_category "; 
		
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(gSAT);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
		
	}

}
