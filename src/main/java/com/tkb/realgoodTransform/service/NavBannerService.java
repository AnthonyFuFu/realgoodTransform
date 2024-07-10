package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.NavBanner;



/**
 * 首頁NavBanner Service介面接口
 * @author felix
 * @version 創建時間：2022-03-07
 */

public interface NavBannerService {
	/**
	 * 取得首頁NavBanner 資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param navBanner
	 * @return List<NavBanner>
	 */
	public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner);
	
	/**
	 * 取得前台首頁NavBanner 清單
	 * @param pageCount
	 * @param pageStart
	 * @param navBanner
	 * @return List<NavBanner>
	 */
	public List<NavBanner> getFrontList(NavBanner navBanner);
	
	public List<NavBanner> getFrontType2List(NavBanner navBanner);
	/**
	 * 取得首頁NavBanner 總筆數
	 * @param navBanner
	 * @return Integer
	 */
	public Integer getCount(NavBanner navBanner);
	
	/**
	 * 取得前台首頁NavBanner 總筆數
	 * @param navBanner
	 * @return Integer
	 */
	public Integer getFrontCount(NavBanner navBanner);

	/**
	 * 取得前台首頁NavBanner show總筆數
	 * @param navBanner
	 * @return Integer
	 */
	public Integer getShowCount(NavBanner navBanner);
	
	
	/**
	 * 取得單筆首頁NavBanner 
	 * @param navBanner
	 * @return NavBanner
	 */
	public NavBanner getData(NavBanner navBanner);
	
	/**
	 * 取得前台單筆首頁NavBanner 
	 * @param navBanner
	 * @return NavBanner
	 */
	public NavBanner getFrontData(NavBanner navBanner);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增首頁NavBanner 
	 * @param navBanner
	 */
	public void add(NavBanner navBanner);
	
	/**
	 * 修改首頁NavBanner 
	 * @param navBanner
	 */
	public void update(NavBanner navBanner);
	
	/**
	 * 修改首頁NavBanner 點擊率
	 * @param navBanner
	 */
	public void updateClickRate(NavBanner navBanner);
	
	/**
	 * 修改NavBanner排序
	 * @param NavBanner
	 */
	public void updateSort(NavBanner navBanner);
	/**
	 * 刪除首頁NavBanner 
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 重新排序NavBanner
	 * @param navBanner
	 */
	public void resetSort();
	
	
	/**
	 * 簡章側邊欄廣告
	 */
	public List<NavBanner> getBrochureFrontList(NavBanner navBanner);
	
	/**
	 * 簡章側邊欄廣告
	 */
	public List<NavBanner> getBrochuregetFrontType2List(NavBanner navBanner);
	
	
	
	
	
//	=======================
	/**
	 * 獲取正是機NavBannerlist
	 */
	public List<Map<String, Object>> getNormalNavBannerList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(NavBanner navBanner);
}
