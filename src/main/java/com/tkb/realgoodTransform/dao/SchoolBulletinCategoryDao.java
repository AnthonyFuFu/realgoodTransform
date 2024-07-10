package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.SchoolBulletinCategory;



public interface SchoolBulletinCategoryDao {
	/**
	 * 取得子層百官網公告類別資料清單
	 * @param schoolBulletinCategory
	 * @return List<SchoolBulletinCategory>
	 */
	public List<SchoolBulletinCategory> getSubList(SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 取得百官網公告類別資料清單(層級)
	 * @param layer
	 * @param schoolBulletinCategory
	 * @return List<SchoolBulletinCategory>
	 */
	public List<SchoolBulletinCategory> getLayerList(String layer, SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 取得百官網公告類別資料清單(分頁)
	 * @param schoolBulletinCategory
	 * @param pageCount
	 * @param pageStart
	 * @return List<SchoolBulletinCategory>
	 */
	public List<SchoolBulletinCategory> getList(int pageCount, int pageStart, SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 取得百官網公告類別總筆數
	 * @param schoolBulletinCategory
	 * @return Integer
	 */
	public Integer getCount(SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 取得單筆百官網公告類別
	 * @param schoolBulletinCategory
	 * @return SchoolBulletinCategory
	 */
	public SchoolBulletinCategory getData(SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 檢查重複名稱
	 * @param schoolBulletinCategory
	 * @return SchoolBulletinCategory
	 */
	public SchoolBulletinCategory checkRepeat(SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增百官網公告類別
	 * @param schoolBulletinCategory
	 */
	public void add(SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 修改百官網公告類別
	 * @param schoolBulletinCategory
	 */
	public void update(SchoolBulletinCategory schoolBulletinCategory);
	
	/**
	 * 刪除百官網公告類別
	 * @param id
	 */
	public void delete(SchoolBulletinCategory schoolBulletinCategory,Integer id);
	
	/**
	 * 重新排序
	 */
	public void resetSort(SchoolBulletinCategory schoolBulletinCategory);
	
	public Integer deleteCheck(SchoolBulletinCategory schoolBulletinCategory);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(SchoolBulletinCategory schoolBulletinCategory);
	
	
	
}
