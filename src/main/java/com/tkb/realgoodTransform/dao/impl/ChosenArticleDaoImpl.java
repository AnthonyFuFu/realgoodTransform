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

import com.tkb.realgoodTransform.dao.ChosenArticleDao;
import com.tkb.realgoodTransform.model.ChosenArticle;



@Repository
public class ChosenArticleDaoImpl implements ChosenArticleDao {

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
	public List<ChosenArticle> getList(int pageCount, int pageStart, ChosenArticle chosenArticle) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT CA.*,CAC.NAME AS CATEGORY_NAME FROM CHOSEN_ARTICLE CA"
				+ " LEFT JOIN CHOSEN_ARTICLE_CATEGORY CAC ON CAC.ID = CA.ARTICLE_CATEGORY "
				+ " WHERE DELETE_STATUS = '0'  ";

		if (chosenArticle.getTitle() != null && !"".equals(chosenArticle.getTitle())) {
			sql += " AND CA.TITLE LIKE ? ";
			args.add("%" + chosenArticle.getTitle() + "%");
		}

		sql += " ORDER BY CA.CREATE_DATE DESC LIMIT ? OFFSET ? ";
		
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class), args.toArray());

	}

	@Override
	public List<ChosenArticle> getFrontList() {

		String sql = " SELECT CA.*,CAC.NAME AS CATEGORY_NAME FROM CHOSEN_ARTICLE CA "
				+ " LEFT JOIN CHOSEN_ARTICLE_CATEGORY CAC ON CA.ARTICLE_CATEGORY = CAC.ID WHERE CA.DISPLAY = '1' "
				+ " AND CA.DELETE_STATUS ='0' " + " AND CA.TOP_STATUS ='1' " + " ORDER BY RANDOM() LIMIT 2";

		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class));

	}

	@Override
	public List<ChosenArticle> getIndexList() {
		
		String sql = " SELECT CA.*,CAC.NAME AS CATEGORY_NAME FROM CHOSEN_ARTICLE "
				+ " CA LEFT JOIN CHOSEN_ARTICLE_CATEGORY CAC ON CA.ARTICLE_CATEGORY = CAC.ID "
				+ " WHERE CA.DISPLAY = '1' " + " AND CA.DELETE_STATUS ='0' " + " AND CA.TOP_STATUS ='1' "
				+ " ORDER BY RANDOM() LIMIT 2 ";
		
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class));


	}

	@Override
	public List<ChosenArticle> getFrontList(int pageCount, int pageStart, ChosenArticle chosenArticle, String search_sort) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT CA.*,CAC.NAME AS CATEGORY_NAME,(SELECT CAC2.NAME FROM CHOSEN_ARTICLE_CATEGORY CAC2 WHERE CAC2.ID = CAC.PARENT_ID)PARENT_CATEGORY_NAME "
				+ " FROM CHOSEN_ARTICLE CA "
				+ " LEFT JOIN CHOSEN_ARTICLE_CATEGORY CAC ON CAC.ID = CA.ARTICLE_CATEGORY  "
				+ " WHERE CA.DISPLAY = '1' " + " AND CA.DELETE_STATUS='0' ";


		if (chosenArticle.getArticle_category() != 0) {
				sql += " AND CA.ARTICLE_CATEGORY = ? ";
				args.add(chosenArticle.getArticle_category());
		}

		sql += " ORDER BY CA.CREATE_DATE DESC LIMIT ? OFFSET ?  ";

		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class), args.toArray());

	}

	@Override
	public List<ChosenArticle> getFrontList(ChosenArticle chosenArticle) {
		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT CA.*, CAC.NAME AS CATEGORY_NAME, "
		        + " (SELECT CAC2.NAME FROM CHOSEN_ARTICLE_CATEGORY CAC2 WHERE CAC2.ID = CAC.PARENT_ID) AS PARENT_CATEGORY_NAME "
		        + " FROM CHOSEN_ARTICLE CA "
		        + " LEFT JOIN CHOSEN_ARTICLE_CATEGORY CAC ON CAC.ID = CA.ARTICLE_CATEGORY "
		        + " WHERE CA.DISPLAY = '1' AND CA.DELETE_STATUS = '0' ";

		if (chosenArticle.getArticle_category() != 0) {
		    if (chosenArticle.getCategory_layer() == 2) {
		        sql += " AND CA.ARTICLE_CATEGORY = ? ";
		        args.add(chosenArticle.getArticle_category());
		    } else if (chosenArticle.getCategory_layer() == 1) {
		        sql += " AND CAC.PARENT_ID = ? ";
		        args.add(chosenArticle.getArticle_category());
		    }
		}

		sql += " ORDER BY CA.CREATE_DATE DESC ";
		sql = " SELECT ROW_NUMBER() OVER() AS ROWNUM, CA.* FROM( " + sql + ") AS CA ";
		
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class), args.toArray());

	}

	@Override
	public Integer getCount(ChosenArticle chosenArticle) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM CHOSEN_ARTICLE WHERE DELETE_STATUS = '0' ";

		if (chosenArticle.getTitle() != null && !"".equals(chosenArticle.getTitle())) {
			sql += " AND TITLE LIKE ? ";
			args.add("%" + chosenArticle.getTitle() + "%");
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());
	}

	@Override
	public Integer getFrontCount(ChosenArticle chosenArticle) {

		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT COUNT(*) FROM CHOSEN_ARTICLE CA "
				+ " LEFT JOIN CHOSEN_ARTICLE_CATEGORY CAC ON CAC.ID = CA.ARTICLE_CATEGORY "
				+ " WHERE CA.DISPLAY = '1' AND CA.DELETE_STATUS='0' ";

		if (chosenArticle.getArticle_category() != 0) {
				sql += " AND CA.ARTICLE_CATEGORY = ? ";
				args.add(chosenArticle.getArticle_category());
		}

		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public ChosenArticle getData(ChosenArticle chosenArticle) {

		String sql = " SELECT * FROM CHOSEN_ARTICLE WHERE ID = ? ";
		
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class),
				new Object[] { chosenArticle.getId() });

	}

	@Override
	public ChosenArticle getFrontData(ChosenArticle chosenArticle) {

		String sql = " SELECT * FROM CHOSEN_ARTICLE WHERE ID = ? ";
		return postgresqlJdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class),
				new Object[] { chosenArticle.getId() });

	}

	@Override
	public Integer getCountListByCategory(int categoryId) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT COUNT(*) AS COUNT FROM CHOSEN_ARTICLE WHERE ARTICLE_CATEGORY = ? AND DELETE_STATUS='0' AND DISPLAY = '1'  ";
		args.add(categoryId);
		
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class, args.toArray());

	}

	@Override
	public Integer getNextId() {

		Integer nextId = null;
		String sql = "SELECT ID FROM CHOSEN_ARTICLE ORDER BY ID DESC LIMIT 1 ";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
		
	}

	@Override
	public void add(ChosenArticle chosenArticle) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(chosenArticle);
		String sql = " INSERT INTO CHOSEN_ARTICLE(ID, ARTICLE_NUM, ARTICLE_CATEGORY, ARTICLE_AUTHOR, TITLE,"
				+ " SUMMARY, QUOTE_CONTENT, CONTENT, IMAGE_URL,CLICK_RATE, TOP_STATUS, DISPLAY,"
				+ " DELETE_STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, LAST_UPDATE, BANNER_URL, EDIT_FROM,ENCRYPTURL, PRODUCT_CATEGORY) "
				+ " VALUES(:id, :article_num, :article_category, :article_author, :title, :summary, "
				+ " :quote_content_string, :content, :image_url,0,:top_status, :display, :delete_status, :create_by, now(), :update_by,"
				+ " now(), now(), :banner_url, :edit_from,:encrypturl, :product_category) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void update(ChosenArticle chosenArticle) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(chosenArticle);
		String sql = " UPDATE CHOSEN_ARTICLE SET ARTICLE_NUM = :article_num, ARTICLE_CATEGORY = :article_category,"
				+ " TITLE = :title, SUMMARY = :summary, CONTENT = :content, IMAGE_URL = :image_url, TOP_STATUS = :top_status, DISPLAY = :display, "
				+ " UPDATE_BY = :update_by,UPDATE_DATE = now(), BANNER_URL = :banner_url, EDIT_FROM = :edit_from , PRODUCT_CATEGORY = :product_category"
				+ " WHERE ID = :id";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}

	@Override
	public void updateClickRate(ChosenArticle chosenArticle) {
		String sql = " UPDATE CHOSEN_ARTICLE SET CLICK_RATE = CLICK_RATE+1 WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] { chosenArticle.getId() });

	}

	@Override
	public void delete(Integer id) {

		String sql = " UPDATE CHOSEN_ARTICLE SET DELETE_STATUS = '1' WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});

	}

	@Override
	public void updateDisplayHide(Integer id) {

		String sql = " UPDATE CHOSEN_ARTICLE SET DISPLAY = '0' WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});

	}

	@Override
	public void updateDisplayShow(Integer id) {

		String sql = " UPDATE CHOSEN_ARTICLE SET DISPLAY = '1' WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});

	}

	@Override
	public Integer getCountListByTop(int articleId) {

		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT COUNT(*) AS COUNT FROM CHOSEN_ARTICLE WHERE TOP_STATUS = '1' AND DELETE_STATUS='0' ";
		if (articleId != 0) {
			sql += " AND ID <> ? ";
			args.add(articleId);

		}
		
		return postgresqlJdbcTemplate.update(sql, new Object[] {args.toArray()});

	}

	@Override
	public List<ChosenArticle> getList(ChosenArticle chosenArticle) {

		String sql = " SELECT * FROM CHOSEN_ARTICLE ";
		
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class));

	}

	@Override
	public void updateId(ChosenArticle chosenArticle) {

		String sql = " UPDATE CHOSEN_ARTICLE SET ENCRYPTURL = ? WHERE ID = ? ";
		
		postgresqlJdbcTemplate.update(sql, new Object[] {chosenArticle.getEncrypt_id(), chosenArticle.getId()});

	}

	@Override
	public List<ChosenArticle> getArticleListForUpdate() {

		String sql = " SELECT * FROM CHOSEN_ARTICLE WHERE DELETE_STATUS = '0' ";
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class));

	}

	@Override
	public void updateQuoteArticle(ChosenArticle chosenArticle) {

		String sql = " UPDATE CHOSEN_ARTICLE SET QUOTE_CONTENT = ?, "
				+ " UPDATE_BY = ?, UPDATE_DATE = now(), LAST_UPDATE = now() WHERE ID = ?";
		
		postgresqlJdbcTemplate.update(sql, new Object[] {chosenArticle.getQuote_content_string(),
				chosenArticle.getUpdate_by(), chosenArticle.getId()});

	}

	@Override
	public List<ChosenArticle> getPrevList(ChosenArticle chosenArticle) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT TITLE,ENCRYPTURL,PRODUCT_CATEGORY FROM CHOSEN_ARTICLE WHERE ID < ? and article_category = ? ORDER BY ID DESC LIMIT 1";
		args.add(chosenArticle.getId());
		
		args.add(chosenArticle.getArticle_category());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class),args.toArray());
	}

	@Override
	public List<ChosenArticle> getNextList(ChosenArticle chosenArticle) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT TITLE,ENCRYPTURL,PRODUCT_CATEGORY FROM CHOSEN_ARTICLE WHERE ID > ? and article_category = ?  LIMIT 1 ";
		args.add(chosenArticle.getId());
		
		args.add(chosenArticle.getArticle_category());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<ChosenArticle>(ChosenArticle.class),args.toArray());
	}

	
	
//	=========
	
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from chosen_article";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(ChosenArticle chosenArticle) {
		String sql = " INSERT into chosen_article (id,article_num,article_category,article_author,title,summary,image_url,click_rate,top_status,display,delete_status,create_by,create_date,update_by,update_date,last_update,banner_url,edit_from,quote_content,content,encrypturl,product_category) VALUES (:id,:article_num,:article_category,:article_author,:title,:summary,:image_url,:click_rate,:top_status,:display,:delete_status,:create_by,CAST(:create_date AS TIMESTAMP),:update_by,:update_date,:last_update,:banner_url,:edit_from,:quote_content,:content,:encrypturl,:product_category)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET article_num = :article_num,article_category = :article_category,article_author = :article_author,title = :title,summary = :summary,image_url = :image_url,click_rate = :click_rate,top_status = :top_status,display = :display,delete_status = :delete_status,create_by = :create_by,create_date = CAST(:create_date AS TIMESTAMP),update_by = :update_by,update_date = :update_date,last_update = :last_update,banner_url = :banner_url,edit_from = :edit_from,quote_content = :quote_content,content = :content,encrypturl = :encrypturl,product_category = :product_category "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(chosenArticle);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}
}
