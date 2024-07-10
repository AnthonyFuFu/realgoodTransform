package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Lectures;



public interface LecturesDao {
	/**
	 * 取得熱門講座資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getList(int pageCount, int pageStart, Lectures lectures);
	
	/**
	 * 取得前台熱門講座清單(依地區類別查詢)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getFrontList(Lectures lectures);
	
	/**
	 * 取得前台熱門講座資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getFrontList(int pageCount, int pageStart, Lectures lectures, String search_sort);
	
	
	/**
	 * 取得熱門講座總筆數
	 * @param lectures
	 * @return Integer
	 */
	public Integer getCount(Lectures lectures);
	
	/**
	 * 取得前台百官網公告總筆數
	 * @param lectures
	 * @return Integer
	 */
	public Integer getFrontCount(Lectures lectures);
	
	
	/**
	 * 取得單筆熱門講座
	 * @param lectures
	 * @return lectures
	 */
	public Lectures getData(Lectures lectures);
	
	/**
	 * 取得前台單筆熱門講座
	 * @param lectures
	 * @return lectures
	 */
	public Lectures getFrontData(Lectures lectures);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增熱門講座
	 * @param lectures
	 */
	public void add(Lectures lectures);
	
	/**
	 * 修改熱門講座
	 * @param lectures
	 */
	public void update(Lectures lectures);
	
	/**
	 * 修改熱門講座點擊率
	 * @param lectures
	 */
	public void updateClickRate(Lectures lectures);
	
	/**
	 * 刪除熱門講座
	 * @param id
	 */
	public void delete(Integer id);		
	
	/**
	 * 置頂次數
	 */
	public Integer checkTopCount(Lectures lectures);	
	
	
	/**
	 * 取得免費課程資料
	 */
	public List<Lectures> getFreeCourse();

	public List<Map<String, Object>> getPlaceName(Integer id);
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Lectures lectures);
	
}
