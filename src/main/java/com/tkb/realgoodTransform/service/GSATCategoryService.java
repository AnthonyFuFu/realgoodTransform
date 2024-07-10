package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.GSATCategory;

/**
 * 類別Service介面接口
 */
public interface GSATCategoryService {

	/**
	 * 取得子層類別資料清單
	 */
	public List<GSATCategory> getSubList(GSATCategory gSATCategory);
	
	/**
	 * 取得類別資料清單(層級)
	 */
	public List<GSATCategory> getLayerList(String layer, GSATCategory gSATCategory);
	
	/**
	 * 取得類別資料清單(分頁)
	 */
	public List<GSATCategory> getList(int pageCount, int pageStart, GSATCategory gSATCategory);
	
	/**
	 * 取得類別總筆數
	 */
	public Integer getCount(GSATCategory gSATCategory);
	
	/**
	 * 取得單筆類別
	 */
	public GSATCategory getData(GSATCategory gSATCategory);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增類別
	 */
	public void add(GSATCategory gSATCategory);
	
	/**
	 * 修改類別
	 */
	public void update(GSATCategory gSATCategory);
	
	/**
	 * 刪除類別
	 */
	public void delete(GSATCategory gSATCategory, Integer id);	
	
	/**
	 * 檢查類別是否重複
	 */
	public String checkGSATCategory(GSATCategory gSATCategory);		
	
	/**
	 * 重新排序
	 */
	public void resetSort(GSATCategory gSATCategory);	
	
	/**
	 * 取得類別ID
	 */
	public Integer getCategoryId(String name);	
	
	
//	==========================
	/**
	 * 獲取正是機Menu
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Menu
	 */
	public void updateNormalData(GSATCategory gSATCategory);
	
}
