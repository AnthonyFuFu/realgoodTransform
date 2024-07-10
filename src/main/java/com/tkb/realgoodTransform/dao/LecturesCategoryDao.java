package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.LecturesCategory;



public interface LecturesCategoryDao {
	/**
	 * 取得子層類別資料清單
	 * @param lecturesCategory
	 * @return List<LecturesCategory>
	 */
	public List<LecturesCategory> getSubList(LecturesCategory lecturesCategory);
	
	/**
	 * 取得類別資料清單(層級)
	 * @param lecturesCategory
	 * @return List<LecturesCategory>
	 */
	public List<LecturesCategory> getLayerList(String layer, LecturesCategory lecturesCategory);
	
	/**
	 * 取得類別資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lecturesCategory
	 * @return List<LecturesCategory>
	 */
	public List<LecturesCategory> getList(int pageCount, int pageStart, LecturesCategory lecturesCategory);
	
	/**
	 * 取得類別總筆數
	 * @param lecturesCategory
	 * @return Integer
	 */
	public Integer getCount(LecturesCategory lecturesCategory);
	
	/**
	 * 取得單筆類別
	 * @param lecturesCategory
	 * @return LecturesCategory
	 */
	public LecturesCategory getData(LecturesCategory lecturesCategory);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增類別
	 * @param lecturesCategory
	 */
	public void add(LecturesCategory lecturesCategory);
	
	/**
	 * 修改類別
	 * @param lecturesCategory
	 */
	public void update(LecturesCategory lecturesCategory);
	
	/**
	 * 刪除類別
	 * @param id
	 */
	public void delete(LecturesCategory lecturesCategory, Integer id);		
	
	/**
	 * 檢查類別是否重複
	 */
	public String checkLecturesCategory(LecturesCategory lecturesCategory);	
	
	/**
	 * 重新排序
	 */
	public void resetSort(LecturesCategory lecturesCategory);
	
	/**
	 * 用於前台資料
	 */
	public List<LecturesCategory> getFrontList(LecturesCategory lecturesCategory);

	/**
	 * 用於前台資料
	 */
	public List<LecturesCategory> getFrontLayerList(String layer);
	
	/**
	 * 刪除前做確認
	 */
	public Integer deleteCheck(LecturesCategory lecturesCategory);
	
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(LecturesCategory lecturesCategory);
	
}
