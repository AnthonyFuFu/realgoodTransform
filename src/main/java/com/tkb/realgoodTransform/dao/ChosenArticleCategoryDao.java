package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.ChosenArticleCategory;



/**
 * 精選文章類別Dao介面接口
 * @author 
 * @version 創建時間：2016-07-06
 */
public interface ChosenArticleCategoryDao {
	
	/**
	 * 取得子層類別資料清單
	 * @param chosenArticleCategory
	 * @return List<ChosenArticleCategory>
	 */
	public List<ChosenArticleCategory> getSubList(ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 取得精選文章類別資料清單(層級)
	 * @param layer
	 * @param chosenArticleCategory
	 * @return List<ChosenArticleCategory>
	 */
	public List<ChosenArticleCategory> getLayerList(String layer, ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 取得精選文章類別資料清單(分頁)
	 * @param chosenArticleCategory
	 * @param pageCount
	 * @param pageStart
	 * @return List<ChosenArticleCategory>
	 */
	public List<ChosenArticleCategory> getList(int pageCount, int pageStart, ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 取得精選文章類別總筆數
	 * @param chosenArticleCategory
	 * @return Integer
	 */
	public Integer getCount(ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 取得單筆精選文章類別
	 * @param chosenArticleCategory
	 * @return ChosenArticleCategory
	 */
	public ChosenArticleCategory getData(ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增精選文章類別
	 * @param chosenArticleCategory
	 */
	public void add(ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 修改精選文章類別
	 * @param chosenArticleCategory
	 */
	public void update(ChosenArticleCategory chosenArticleCategory);
	
	/**
	 * 刪除精選文章類別
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 依名稱取得精選文章類別筆數
	 * @param categoryName
	 * @return Integer
	 */
	public Integer getCountByName(String categoryName);
	
	/**
	 * 重新排序
	 */
	public void resetSort(ChosenArticleCategory chosenArticleCategory);
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(ChosenArticleCategory chosenArticleCategory);
	
}
