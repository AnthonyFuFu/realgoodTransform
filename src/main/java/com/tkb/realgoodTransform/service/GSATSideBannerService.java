package com.tkb.realgoodTransform.service;

import java.util.List;

import com.tkb.realgoodTransform.model.NavBanner;

/**
 * 首頁NavBanner Service介面接口
 */
public interface GSATSideBannerService {
	
	/**
	 * 取得首頁NavBanner 資料清單(分頁)
	 */
	public List<NavBanner> getList(int pageCount, int pageStart, NavBanner navBanner);
	
	/**
	 * 取得前台首頁NavBanner 清單
	 */
	public List<NavBanner> getFrontList(NavBanner navBanner);
	
	public List<NavBanner> getFrontType2List(NavBanner navBanner);
	/**
	 * 取得首頁NavBanner 總筆數
	 */
	public Integer getCount(NavBanner navBanner);
	
	/**
	 * 取得前台首頁NavBanner 總筆數
	 */
	public Integer getFrontCount(NavBanner navBanner);

	/**
	 * 取得前台首頁NavBanner show總筆數
	 */
	public Integer getShowCount(NavBanner navBanner);
	
	
	/**
	 * 取得單筆首頁NavBanner
	 */
	public NavBanner getData(NavBanner navBanner);
	
	/**
	 * 取得前台單筆首頁NavBanner
	 */
	public NavBanner getFrontData(NavBanner navBanner);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增首頁NavBanner 
	 */
	public void add(NavBanner navBanner);
	
	/**
	 * 修改首頁NavBanner
	 */
	public void update(NavBanner navBanner);
	
	/**
	 * 修改首頁NavBanner 點擊率
	 */
	public void updateClickRate(NavBanner navBanner);
	
	/**
	 * 修改NavBanner排序
	 */
	public void updateSort(NavBanner navBanner);
	/**
	 * 刪除首頁NavBanner
	 */
	public void delete(Integer id);
	
	/**
	 * 重新排序NavBanner
	 */
	public void resetSort();
	
	
}

