package com.tkb.realgoodTransform.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.Winner;
import com.tkb.realgoodTransform.model.WinnerCategory;



public interface WinnerCategoryService {
	/**
	 * 取得子層類別資料清單
	 * @param winnerCategory
	 * @return List<WinnerCategory>
	 */
	public List<WinnerCategory> getSubList(WinnerCategory winnerCategory);
	
	/**
	 * 取得類別資料清單(層級)
	 * @param winnerCategory
	 * @return List<WinnerCategory>
	 */
	public List<WinnerCategory> getLayerList(String layer, WinnerCategory winnerCategory);

	/**
	 * 取得類別資料清單(層級)
	 * @param layer
	 * @return layer
	 */
	public List<WinnerCategory> getLayerList(String layer);
	
	/**
	 * 取得類別資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param winnerCategory
	 * @return List<WinnerCategory>
	 */
	public List<WinnerCategory> getList(int pageCount, int pageStart, WinnerCategory winnerCategory);
	
	/**
	 * 取得類別總筆數
	 * @param winnerCategory
	 * @return Integer
	 */
	public Integer getCount(WinnerCategory winnerCategory);
	
	/**
	 * 取得單筆類別
	 * @param winnerCategory
	 * @return WinnerCategory
	 */
	public WinnerCategory getData(WinnerCategory winnerCategory);
	
	/**
	 * 檢查重複名稱
	 * @param winnerCategory
	 * @return WinnerCategory
	 */
	public WinnerCategory checkRepeat(WinnerCategory winnerCategory);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增類別
	 * @param winnerCategory
	 */
	public void add(WinnerCategory winnerCategory);
	
	/**
	 * 修改類別
	 * @param winnerCategory
	 */
	public void update(WinnerCategory winnerCategory);
	
	/**
	 * 刪除類別
	 * @param id
	 */
	public void delete(WinnerCategory winnerCategory ,Integer id);
	
	/**
	 * 重新排序
	 */
	public void resetSort(WinnerCategory winnerCategory);
	/**
	 * 新增功能商業邏輯
	 * @param winnerCategory user
	 */
	public void addSubmitFunction(WinnerCategory winnerCategory, User user);
	/**
	 * 檢查重複類別名稱商業邏輯
	 */
	public String checkRepeatFunction(String name, String layer, WinnerCategory winnerCategory);
	/**
	 * 修改頁面商業邏輯
	 * @param winnerCategory user
	 */
	public WinnerCategory updateFunction (WinnerCategory winnerCategory);
	/**
	 * 修改功能商業邏輯
	 * @param winnerCategory user
	 */
	public void updateSubmitFunction(WinnerCategory winnerCategory, User user);
	/**
	 * 刪除功能商業邏輯
	 * @param winnerCategory 
	 */
	public void deleteFunction(WinnerCategory winnerCategory, String deleteList, User user) throws IOException;
	/**
	 * 檢查類別商業邏輯
	 */
	public String checkDataFunction (WinnerCategory winnerCategory, Winner winner, String checkList);
	/**
	 * 重新排序功能商業邏輯
	 * @param winnerCategory 
	 */
	public void resetSortFunction(WinnerCategory winnerCategory);
	/**
	 * 取得名稱
	 * @param winnerCategory 
	 */
	public String getCategoryName(String category,WinnerCategory winnerCategory);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(WinnerCategory winerCategory);
}
