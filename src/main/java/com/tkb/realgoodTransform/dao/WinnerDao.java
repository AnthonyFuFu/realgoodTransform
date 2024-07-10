package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.CourseOverviewCourseCategory;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;



public interface WinnerDao {
	/**
	 * 取得贏家經驗談資料清單(分頁)
	 * 
	 * @param winner
	 * @param pageCount
	 * @param pageStart
	 * @return List<Winner>
	 */
	public List<Winner> getList(int pageCount, int pageStart, Winner winner);

	/**
	 * 取得贏家經驗談資料清單(年度專用)
	 * 
	 * @param winner
	 * @param pageCount
	 * @param pageStart
	 * @return List<Winner>
	 */
	public List<Winner> getList(Winner winner);

	/**
	 * 取得贏家經驗談資料清單(check類別專用)
	 * 
	 * @param winner
	 * @param pageCount
	 * @param pageStart
	 * @return List<Winner>
	 */
	public List<Winner> getSubList(Winner winner);

	/**
	 * 取得贏家經驗談資料清單(依類別地區查詢)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Winner>
	 */
	public List<Winner> getFrontList(Winner winner);

	/**
	 * 取得前台贏家經驗談資料清單(分頁)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Winner>
	 */
	public List<Winner> getFrontList(int pageCount, int pageStart, Winner winner, String search_sort);

	/**
	 * 取得前台贏家經驗談資料清單(類別專用)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @param winnerList
	 * @return List<Winner>
	 */
	public List<Winner> getFrontList(int pageCount, int pageStart, Winner winner,
			List<WinnerCategory> winnerCategoryList, String search_sort);

	/**
	 * 取得贏家經驗談總筆數
	 * 
	 * @param winner
	 * @return Integer
	 */
	public Integer getCount(Winner winner);

	/**
	 * 取得贏家經驗談有被使用的category數量_父層
	 * 
	 * @param winner
	 * @return Integer
	 */
	public Integer getUseCount_parent(int count);

	/**
	 * 取得贏家經驗談有被使用的category數量_子層
	 * 
	 * @param winner
	 * @return Integer
	 */
	public Integer getUseCount_child(int count);

	/**
	 * 取得前台贏家經驗談總筆數
	 * 
	 * @param winner
	 * @return Integer
	 */
	public Integer getFrontCount(Winner winner);

	/**
	 * 取得前台贏家經驗談總筆數(類別專用)
	 * 
	 * @param winner
	 * @param winnerCategoryList
	 * @return
	 */
	public Integer getFrontCount(Winner winner, List<WinnerCategory> winnerCategoryList);

	/**
	 * 取得單筆贏家經驗談
	 * 
	 * @param winner
	 * @return Winner
	 */
	public Winner getData(Winner winner);

	/**
	 * 取得前台單筆贏家經驗談
	 * 
	 * @param winner
	 * @return Winner
	 */
	public Winner getFrontData(Winner winner);

	/**
	 * 取得下一筆ID
	 * 
	 * @return Integer
	 */
	public Integer getNextId();

	/**
	 * 新增贏家經驗談
	 * 
	 * @param winner
	 */
	public void add(Winner winner);

	/**
	 * 修改贏家經驗談
	 * 
	 * @param winner
	 */
	public void update(Winner winner);

	/**
	 * 修改贏家經驗談點擊率
	 * 
	 * @param newExam
	 */
	public void updateClickRate(Winner winner);

	/**
	 * 刪除贏家經驗談
	 * 
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * 取得贏家經驗談資料清單(影音年度專用)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Winner>
	 */
	public List<Winner> getVideoYearList(Winner winner);

	/**
	 * 取得前台贏家經驗談資料清單(類別專用)
	 * 
	 * @param winner
	 * @param winnerList
	 * @return List<Map>
	 */
	public List<Winner> getRandomList(Winner winner);

	/**
	 * 取得影音專區資料清單(抓取同類別資料)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Winner>
	 */
	public List<Winner> getVideoList(Winner winner);

	/**
	 * 首頁影音專區清單
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Map>
	 */
	public List<Winner> getVideoIndexList(Winner winner);

	/**
	 * 熱門影音專區清單
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Winner>
	 */
	public List<Winner> getVideoHotList(Winner winner);

	/**
	 * 取得影片專區小影片
	 */
	public List<Winner> getFrontVideoList(Winner winner);

	/**
	 * 取得推薦影片
	 */
	public List<Winner> getRecommendVideoList(Winner winner);

	/**
	 * 取得前一頁
	 */
	public List<Winner> getPrevList(Winner winner);

	/**
	 * 取得下一頁
	 */
	public List<Winner> getNextList(Winner winner);
	
	/**
	 * 取得影音專區資料清單(抓取同類別資料)
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Map>
	 */
	public List<Winner> getVideoMainList(Winner winner);
	
	/**
	 * 取得影音專區資料清單(抓取同類別資料)
	 * @param pageCount
	 * @param pageStart
	 * @param winner
	 * @return List<Winner>
	 */
	public List<Winner> getVideoCategoryList(Winner winner);

	public Winner getCourseOverviewVideoData(Winner winner, CourseOverviewCourseCategory courseOverviewCourseCategory);

	public List<Map<String, Object>> getCourseOverviewVideoList(Winner winner,
			CourseOverviewCourseCategory courseOverviewCourseCategory);
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Winner winner);
}
