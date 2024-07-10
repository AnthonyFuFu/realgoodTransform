package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.ChosenArticle;



public interface ChosenArticleDao {

	/**
	 * 取得精選文章資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param chosenArticle
	 * @return List<ChosenArticle>
	 */
	public List<ChosenArticle> getList(int pageCount, int pageStart, ChosenArticle chosenArticle);
	
	/**
	 * 取得首頁精選文章清單
	 * @param pageCount
	 * @param pageStart
	 * @param chosenArticle
	 * @return List<ChosenArticle>
	 */
	public List<ChosenArticle> getFrontList();
    /**
     * 取得首頁精選文章清單
     * @param pageCount
     * @param pageStart
     * @param chosenArticle
     * @return List<ChosenArticle>
     */
    public List<ChosenArticle> getIndexList();	
	/**
	 * 取得前台精選文章資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param chosenArticle
	 * @return List<ChosenArticle>
	 */
	public List<ChosenArticle> getFrontList(int pageCount, int pageStart, ChosenArticle chosenArticle, String search_sort);
	
	/**
	 * 取得前台精選文章資料清單(無分頁)
	 * @param chosenArticle
	 * @return List<ChosenArticle>
	 */
	public List<ChosenArticle> getFrontList(ChosenArticle chosenArticle);
	
	/**
	 * 取得精選文章總筆數
	 * @param chosenArticle
	 * @return Integer
	 */
	public Integer getCount(ChosenArticle chosenArticle);
	
	/**
	 * 取得前台總筆數
	 * @param chosenArticle
	 * @return Integer
	 */
	public Integer getFrontCount(ChosenArticle chosenArticle);
	
	/**
	 * 取得單筆金榜
	 * @param chosenArticle
	 * @return chosenArticle
	 */
	public ChosenArticle getData(ChosenArticle chosenArticle);
	
	/**
	 * 取得前台單筆金榜
	 * @param chosenArticle
	 * @return chosenArticle
	 */
	public ChosenArticle getFrontData(ChosenArticle chosenArticle);
	
	/**
	 * 依分類取得精選文章數量
	 * @return Integer
	 */
	public Integer getCountListByCategory(int categoryId);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();

	/**
	 * 新增精選文章
	 * @param chosenArticle
	 */
	public void add(ChosenArticle chosenArticle);
	
	/**
	 * 修改精選文章
	 * @param chosenArticle
	 */
	public void update(ChosenArticle chosenArticle);
	
	/**
	 * 修改精選文章點擊率
	 * @param chosenArticle
	 */
	public void updateClickRate(ChosenArticle chosenArticle);
	
	/**
	 * 刪除精選文章
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 精選文章下架
	 * @param id
	 */
	public void updateDisplayHide(Integer id);
	
	/**
	 * 精選文章上架
	 * @param id
	 */
	public void updateDisplayShow(Integer id);
	
	/**
	 * 依置頂取得精選文章數量
	 * @return Integer
	 */
	public Integer getCountListByTop(int articleId);
	/**
     * 取得精選文章資料清單(全部)
     * @param chosenArticle
     * @return List<ChosenArticle>
     */
  public List<ChosenArticle> getList(ChosenArticle chosenArticle);
  /**
   * 修改精選文章
   * @param chosenArticle
   */
  public void updateId(ChosenArticle chosenArticle);
  
  /**
   * 取得精選文章資料清單(更新文章系統文章用)
   * @return List<ChosenArticle>
   */
  public List<ChosenArticle> getArticleListForUpdate();
  
  /**
	 * 修改精選文章的引用文章
	 * @param chosenArticle
	 */
	public void updateQuoteArticle(ChosenArticle chosenArticle);
	
	/**
	 * 取得前一頁
	 */
	public List<ChosenArticle> getPrevList(ChosenArticle chosenArticle);

	/**
	 * 取得下一頁
	 */
	public List<ChosenArticle> getNextList(ChosenArticle chosenArticle);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(ChosenArticle chosenArticle);
	
    
}
