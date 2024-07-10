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

import com.tkb.realgoodTransform.dao.WinnerDao;
import com.tkb.realgoodTransform.model.CourseOverviewCourseCategory;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;



@Repository
public class WinnerDaoImpl implements WinnerDao {

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
	public List<Winner> getList(int pageCount, int pageStart, Winner winner) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT W.*, C.NAME AS CATEGORY_NAME, C1.NAME AS PARENT_NAME "
				         + "FROM WINNER W "
				         + "LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY AS INTEGER) "
				         + "LEFT JOIN WINNER_CATEGORY C1 ON C1.ID = CAST(W.PARENT_CATEGORY AS INTEGER) "
				         + "WHERE 1=1 ");
		if(winner.getName() != null && !"".equals(winner.getName())) {
			sql.append("AND W.NAME LIKE ? ");
			args.add("%" + winner.getName() + "%");
		}
		if(winner.getYear() != null && !"".equals(winner.getYear())) {
			sql.append("AND W.YEAR LIKE ? ");
			args.add("%" + winner.getYear() + "%");
		}
		if(winner.getCategory() != null && !"".equals(winner.getCategory())) {
			sql.append("AND W.CATEGORY = ? ");
			args.add(winner.getCategory());
		}
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql.append("AND W.PARENT_CATEGORY = ? ");
			args.add(winner.getParent_category());
		}
		
		sql.append("ORDER BY W.CREATE_DATE DESC LIMIT ? OFFSET ? ");
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql.toString(),new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getList(Winner winner) {
		String sql = "SELECT DISTINCT YEAR FROM WINNER WHERE 1 = 1 ORDER BY YEAR ASC";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class));
	}

	@Override
	public List<Winner> getSubList(Winner winner) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT * FROM WINNER WHERE CATEGORY = ? ";
		args.add(winner.getCategory());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class), args.toArray());
	}

	@Override
	public List<Winner> getFrontList(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT W.*,C.NAME AS CATE_NAME FROM WINNER W "
				   + "LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY AS INTEGER) "
				   + "WHERE W.PARENT_CATEGORY = ? AND W.CATEGORY = ? AND SHOW = 1 ";
		args.add(winner.getParent_category());
		args.add(winner.getCategory());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getFrontList(int pageCount, int pageStart, Winner winner, String search_sort) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT W.*, C.NAME AS CATEGORY_NAME, C1.NAME AS PARENT_NAME "
				   + "FROM WINNER W "
				   + " LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY AS INTEGER) "
				   + " LEFT JOIN WINNER_CATEGORY C1 ON C1.ID = CAST(W.PARENT_CATEGORY AS INTEGER) "
				   + " WHERE SHOW = 1 ";
		if(winner.getCategory() != null && !"".equals(winner.getCategory())) {
			sql += " AND W.CATEGORY = ? ";
			args.add(winner.getCategory());
		}
			
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql += " AND W.PARENT_CATEGORY = ? ";
			args.add(winner.getParent_category());
		}
		if(winner.getYear() != null && !"".equals(winner.getYear())) {
			sql += " AND W.YEAR = ? ";
			args.add(winner.getYear());
		}
		
		
//		if("".equals(search_sort) || search_sort == null ) {
			sql += " ORDER BY W.CREATE_DATE DESC ";
//		} else {
//			sql += " ORDER BY W.CREATE_DATE DESC ";
//		}
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getFrontList(int pageCount, int pageStart, Winner winner,
			List<WinnerCategory> winnerCategoryList, String search_sort) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT W.*, C.NAME AS CATEGORY_NAME, C1.NAME AS PARENT_NAME "
				   + "FROM WINNER W "
				   + " LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY AS INTEGER) "
				   + " LEFT JOIN WINNER_CATEGORY C1 ON C1.ID = CAST(W.PARENT_CATEGORY AS INTEGER) "
				   + " WHERE SHOW = 1 ";
		if(winner.getCategory() != null && !"".equals(winner.getCategory())) {
			sql += " AND W.CATEGORY = ? ";
			args.add(winner.getCategory());
		}
			
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql += " AND W.PARENT_CATEGORY = ? ";
			args.add(winner.getParent_category());
		}
		if(winner.getYear() != null && !"".equals(winner.getYear())) {
			sql += " AND W.YEAR = ? ";
			args.add(winner.getYear());
		}
		
		if(winnerCategoryList != null && winnerCategoryList.size() > 0) {
			sql += " AND (";
			for(int i=0; i<winnerCategoryList.size(); i++) {
				sql += " CAST(W.CATEGORY AS INTEGER) = ? ";
				args.add(winnerCategoryList.get(i).getId());
				if(i < winnerCategoryList.size()-1) {
					sql += " OR ";
				}
			}
			sql += " ) ";
		}
		
//		if("".equals(search_sort) || search_sort == null ) {
//			sql += " ORDER BY W.CREATE_DATE DESC ";
//		} else {
			sql += " ORDER BY W.CREATE_DATE DESC ";
//		}
		sql += "LIMIT ? OFFSET ? ";
		args.add(pageCount);
		args.add(pageStart);
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public Integer getCount(Winner winner) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM WINNER WHERE 1 = 1 ");
		if(winner.getName() != null && !"".equals(winner.getName())) {
			sql.append("AND NAME LIKE ? ");
			args.add("%" + winner.getName() + "%");
		}
		if(winner.getYear() != null && !"".equals(winner.getYear())) {
			sql.append("AND YEAR LIKE ? ");
			args.add("%" + winner.getYear() + "%");
		}
		if(winner.getCategory() != null && !"".equals(winner.getCategory())) {
			sql.append("AND CATEGORY = ? ");
			args.add(winner.getCategory());
		}
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql.append("AND PARENT_CATEGORY = ? ");
			args.add(winner.getParent_category());
		}
		return postgresqlJdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
	}

	@Override
	public Integer getUseCount_parent(int count) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM WINNER WHERE CAST(PARENT_CATEGORY AS INTEGER) = ? ";
		args.add(count);
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,args.toArray());
	}

	@Override
	public Integer getUseCount_child(int count) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM WINNER WHERE CAST(CATEGORY AS INTEGER) = ? ";
		args.add(count);
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,args.toArray());
	}

	@Override
	public Integer getFrontCount(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT COUNT(*) FROM WINNER W "
				   + "LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY AS INTEGER) "
				   + "WHERE SHOW = 1 ";
		if(winner.getCategory() != null && !"".equals(winner.getCategory())) {
			sql += "AND W.CATEGORY = ? ";
			args.add(winner.getCategory());
		}
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql += "AND CAST(C.PARENT_ID AS CHARACTER VARYING) = ? ";
			args.add(winner.getParent_category());
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,args.toArray());
	}

	@Override
	public Integer getFrontCount(Winner winner, List<WinnerCategory> winnerCategoryList) {
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT COUNT(*) FROM WINNER W "
				   + " LEFT JOIN WINNER_CATEGORY C1 ON C1.ID = CAST(W.CATEGORY AS INTEGER) "
				   + " WHERE SHOW = 1 ";
		if(winnerCategoryList != null && winnerCategoryList.size() > 0) {
			sql += " AND (";
			for(int i=0; i<winnerCategoryList.size(); i++) {
				sql += " CAST(W.CATEGORY AS INTEGER) = ? ";
				args.add(winnerCategoryList.get(i).getId());
				if(i < winnerCategoryList.size()-1) {
					sql += " OR ";
				}
			}
			sql += " ) ";
		}

		if(winner.getCategory() != null && !"".equals(winner.getCategory())) {
			sql += " AND W.CATEGORY = ? ";
			args.add(winner.getCategory());
		}
		
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql += " AND CAST(C1.PARENT_ID AS CHARACTER VARYING) = ? ";
			args.add(winner.getParent_category());
		}
		return postgresqlJdbcTemplate.queryForObject(sql, Integer.class,args.toArray());
	}

	@Override
	public Winner getData(Winner winner) {
		List<Object> args = new ArrayList<Object>();
		String sql = "SELECT W.*, C.NAME AS CATEGORY_NAME, C1.NAME AS PARENT_NAME "
				   + "FROM WINNER W "
				   + "LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY AS INTEGER) "
			       + "LEFT JOIN WINNER_CATEGORY C1 ON C1.ID = CAST(W.PARENT_CATEGORY AS INTEGER) "
				   + "WHERE W.ID = ? ";
		args.add(winner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public Winner getFrontData(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM WINNER WHERE ID = ? ";
		args.add(winner.getId());
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public Integer getNextId() {
		Integer nextId= null;
		String sql = "SELECT ID FROM WINNER ORDER BY ID DESC LIMIT 1";
		try {
			nextId = postgresqlJdbcTemplate.queryForObject(sql, Integer.class) + 1;
		} catch (Exception e) {
			nextId = 1;
		}
		return nextId;
	}

	@Override
	public void add(Winner winner) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(winner);
		String sql = "INSERT INTO WINNER(ID, CATEGORY, SUMMARY, SHOW, PHOTO, VIDEO, PARENT_CATEGORY, "
				   + "CATEGORY_THREE, IMAGE, YEAR, NAME, ADMITTED, ORIGINAL, CLICK_RATE, CREATE_BY, CREATE_DATE, "
				   + "UPDATE_BY, UPDATE_DATE, ENCRYPTURL, SHOW_INDEX, VIDEO_IMAGE, PRODUCT_CATEGORY) VALUES "
				   + "(:id, :category, :summary, :show, :photo, :video, :parent_category, :category_three , :image, "
				   + ":year, :name, :admitted, :original, 0, :create_by, now(), :update_by, "
				   + "now(), :encrypturl, :show_index, :video_image, :product_category)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		postgresqlJdbcNameTemplate.update(sql, parameter, keyHolder);
	}
	
	@Override
	public void update(Winner winner) {
		SqlParameterSource parameter = new BeanPropertySqlParameterSource(winner);
		String sql = "UPDATE WINNER SET CATEGORY = :category, NAME = :name, ADMITTED = :admitted, ORIGINAL = :original, "
				   + "SUMMARY = :summary, SHOW = :show, PHOTO = :photo, VIDEO = :video, PARENT_CATEGORY = :parent_category, "
				   + "CATEGORY_THREE= :category_three , IMAGE = :image, YEAR = :year, UPDATE_BY = :update_by, UPDATE_DATE = NOW(), "
				   + "SHOW_INDEX = :show_index, VIDEO_IMAGE = :video_image, PRODUCT_CATEGORY = :product_category  WHERE ID = :id ";
		postgresqlJdbcNameTemplate.update(sql, parameter);
	}
	
	@Override
	public void updateClickRate(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "UPDATE WINNER SET CLICK_RATE = CLICK_RATE + 1 WHERE ID = ? ";
		args.add(winner.getId());
		postgresqlJdbcTemplate.update(sql,args.toArray());
		
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM WINNER WHERE ID = ? ";
		postgresqlJdbcTemplate.update(sql, new Object[] {id});

	}

	@Override
	public List<Winner> getVideoYearList(Winner winner) {
		String sql = "SELECT DISTINCT YEAR FROM WINNER WHERE VIDEO IS NOT NULL ORDER BY YEAR ASC";
		return postgresqlJdbcTemplate.query(sql,new BeanPropertyRowMapper<Winner>(Winner.class));
	}

	@Override
	public List<Winner> getVideoList(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT W.*,C.NAME AS CATE_NAME FROM WINNER W "
				   + "LEFT JOIN WINNER_CATEGORY C ON CAST(W.PARENT_CATEGORY AS INTEGER) = C.ID "
				   + "WHERE W.SHOW = 1 AND W.VIDEO IS NOT NULL  ";
		if(winner.getParent_category() != null && !"".equals(winner.getParent_category())) {
			sql += " AND PARENT_CATEGORY = ? ";
			args.add(winner.getParent_category());
		}
		if(winner.getYear() != null && !"".equals(winner.getYear())) {
			sql += " AND YEAR = ? ";
			args.add(winner.getYear());
		}
		sql +=" ORDER BY W.CREATE_DATE DESC ";
		if(winner.getCategory() != null && !"".equals(winner.getCategory())&&winner.getCategory().equals("0")) {
			sql += " LIMIT 5 ";
		}
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getVideoIndexList(Winner winner) {
		String sql = "SELECT * FROM WINNER WHERE SHOW = 1 AND SHOW_INDEX = 1 ORDER BY RANDOM() LIMIT  1 ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class));
	}

	@Override
	public List<Winner> getVideoHotList(Winner winner) {
		String sql = "SELECT * FROM WINNER WHERE SHOW = '1' AND SHOW_INDEX = '1' ORDER BY RANDOM() LIMIT 5";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class));
	}

	@Override
	public List<Winner> getFrontVideoList(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT * FROM WINNER WHERE SHOW = 1 AND SHOW_INDEX = 1 AND ID != ? ORDER BY RANDOM() LIMIT 4;";
		args.add(winner.getId());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getRecommendVideoList(Winner winner) {
		String sql = "SELECT * FROM WINNER WHERE SHOW = 1 AND SHOW_INDEX = 1 ";
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class));
	}

	@Override
	public List<Winner> getPrevList(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT CATEGORY,NAME, ENCRYPTURL,PRODUCT_CATEGORY  FROM WINNER WHERE ID > ?  AND SHOW = '1' LIMIT 1 ";
		args.add(winner.getId());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getNextList(Winner winner) {
		List<Object>args = new ArrayList<>();
		String sql = "SELECT CATEGORY,NAME, ENCRYPTURL,PRODUCT_CATEGORY FROM WINNER WHERE ID < ? AND SHOW = '1' ORDER BY ID DESC LIMIT 1 ";
		args.add(winner.getId());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getRandomList(Winner winner) {
        List<Object> args = new ArrayList<Object>();
        
        String sql = " SELECT * FROM WINNER WHERE SHOW = 1 ORDER BY RANDOM()";

		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public List<Winner> getVideoMainList(Winner winner) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM WINNER WHERE ID = ? ";
		
		args.add(winner.getId());
		
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
		
	}

	@Override
	public List<Winner> getVideoCategoryList(Winner winner) {
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM WINNER WHERE CATEGORY = ? AND SHOW = 1 AND ID != ? AND VIDEO IS NOT NULL ";
		args.add(winner.getCategory());
		args.add(winner.getId());
		return postgresqlJdbcTemplate.query(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());
	}

	@Override
	public Winner getCourseOverviewVideoData(Winner winner, CourseOverviewCourseCategory courseOverviewCourseCategory) {
		List<Object> args = new ArrayList<Object>();

		String sql = " SELECT W.* "
				   + " FROM WINNER W"
				   + " LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY_THREE AS INTEGER) "
				   + " WHERE W.SHOW = 1 "
				   + " AND W.VIDEO != ''";
				   
			if(courseOverviewCourseCategory.getLayer().equals("2")){
				sql +=" AND ( ";
				for(int i = 0; i < courseOverviewCourseCategory.getSmallCategoryList().size() ; i++){
					
					sql += " C.CATEGORY_CODE = ? ";
					args.add(courseOverviewCourseCategory.getSmallCategoryList().get(i).get("ID"));
					
					if(courseOverviewCourseCategory.getSmallCategoryList().size()!=(i+1)){
						sql +=" OR ";
					}
				}
				sql +=" ) ";
			}else{
				sql += " AND C.CATEGORY_CODE = ? ";
				args.add(courseOverviewCourseCategory.getId());
			}

				sql += " ORDER BY RANDOM() LIMIT 1";
		
		return postgresqlJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Winner>(Winner.class),args.toArray());

	}

	@Override
	public List<Map<String, Object>> getCourseOverviewVideoList(Winner winner,
			CourseOverviewCourseCategory courseOverviewCourseCategory) {
	List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT W.* "
				   + " FROM WINNER W "
				   + " LEFT JOIN WINNER_CATEGORY C ON C.ID = CAST(W.CATEGORY_THREE AS INTEGER) "
				   + " WHERE W.SHOW = 1 ";
		
		if(winner.getId()!=null && winner.getId()!=0) {
			sql += "AND W.ID <> ?";
			args.add(winner.getId());
		}
						
		if(courseOverviewCourseCategory.getLayer().equals("2")){
			
			if(courseOverviewCourseCategory.getSmallCategoryList().size() == 0) return new ArrayList<>();
			
			sql +=" AND ( ";
			for(int i = 0; i < courseOverviewCourseCategory.getSmallCategoryList().size() ; i++){
				
				sql += " C.CATEGORY_CODE = ? ";
				args.add(courseOverviewCourseCategory.getSmallCategoryList().get(i).get("ID"));
				
				if(courseOverviewCourseCategory.getSmallCategoryList().size()!=(i+1)){
					sql +=" OR ";
				}
			}
			sql +=" ) ";
		}else{
			sql += " AND C.CATEGORY_CODE = ? ";
			args.add(courseOverviewCourseCategory.getId());
		}
		
		sql +=" ORDER BY RANDOM() OFFSET 0 LIMIT 3";
		
		return postgresqlJdbcTemplate.queryForList(sql,args.toArray());
	}

	
//	==========
	
	
	@Override
	public List<Map<String, Object>> getNormalList() {
		String sql = "select * from winner";
		return oracJdbcTemplate.queryForList(sql);
//		return null;
	}

	@Override
	public void updateNormalData(Winner winner) {
		String sql = " INSERT into winner (id,image,click_rate,year,category,create_by,create_date,update_by,update_date,name,admitted,original,summary,show,photo,video,parent_category,encrypturl,stick_top,show_index,video_image,category_three,product_category) VALUES (:id,:image,:click_rate,:year,:category,:create_by,CAST(:create_date AS TIMESTAMP),:update_by,:update_date,:name,:admitted,:original,:summary,:show,:photo,:video,:parent_category,:encrypturl,:stick_top,:show_index,:video_image,:category_three,:product_category)  "
				+ " ON CONFLICT(id) "
				+ " DO UPDATE SET image = :image,click_rate = :click_rate,year = :year,category = :category,create_by = :create_by,create_date = CAST(:create_date AS TIMESTAMP),update_by = :update_by,update_date = :update_date,name = :name,admitted = :admitted,original = :original,summary = :summary,show = :show,photo = :photo,video = :video,parent_category = :parent_category,encrypturl = :encrypturl,stick_top = :stick_top,show_index = :show_index,video_image = :video_image,category_three = :category_three,product_category = :product_category "; 
	
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(winner);
		
		postgresqlJdbcNameTemplate.update(sql,sqlParameterSource);
	}

	



}
