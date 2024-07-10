package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.GSATMenu;

/**
 * 類別Service介面接口
 */
public interface GSATMenuService {

	/**
	 * 取得子層類別資料清單
	 */
	public List<GSATMenu> getSubList(GSATMenu gSATMenu);
	
	/**
	 * 取得類別資料清單(層級)
	 */
	public List<GSATMenu> getLayerList(String layer, GSATMenu gSATMenu);
	
	/**
	 * 取得類別資料清單(分頁)
	 */
	public List<GSATMenu> getList(GSATMenu gSATMenu);
	
	public List<GSATMenu> getList(int pageCount, int pageStart,GSATMenu gSATMenu);
	/**
	 * 取得類別總筆數
	 */
	public Integer getCount(GSATMenu gSATMenu);
	
	/**
	 * 取得單筆類別
	 */
	public GSATMenu getData(GSATMenu gSATMenu);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增類別
	 */
	public void add(GSATMenu gSATMenu);
	
	/**
	 * 修改類別
	 */
	public void update(GSATMenu gSATMenu);
	
	/**
	 * 刪除類別
	 */
	public void delete(GSATMenu gSATMenu, Integer id);	
	
	/**
	 * 檢查類別是否重複
	 */
	public String checkGSATMenu(GSATMenu gSATMenu);		
	
	/**
	 * 重新排序
	 */
	public void resetSort(GSATMenu gSATMenu);	
	
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
	public void updateNormalData(GSATMenu gSATMenu);
	
}
