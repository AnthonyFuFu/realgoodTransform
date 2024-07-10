package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Banner;



public interface BannerDao {
	/**
	 * 取得首頁Banner 資料清單
	 * @param banner
	 * @return List<Banner>
	 */
	public List<Banner> getList(int pageCount, int pageStart, Banner banner);
	
	/**
	 * 取得首頁Banner 前台資料清單
	 * @param banner
	 * @return List<Banner>
	 */
	public List<Banner> getFrontList(Banner banner);
	
	/**
	 * 取得首頁Banner 總筆數
	 * @param banner
	 * @return Integer
	 */
	public Integer getCount(Banner banner);
	
	
	/**
	 * 取得單筆首頁Banner 
	 * @param banner
	 * @return Banner
	 */
	public Banner getData(Banner banner);
	
	/**
	 * 取得前台單筆首頁Banner 
	 * @param banner
	 * @return Banner
	 */
	public Banner getFrontData(Banner banner);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增首頁Banner 
	 * @param banner
	 */
	public void add(Banner banner);
	
	/**
	 * 修改首頁Banner 
	 * @param banner
	 */
	public void update(Banner banner);
	
	
	/**
	 * 刪除首頁Banner 
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 重新排序Banner
	 * @param banner
	 */
	public void resetSort();
	
	/**
	 * 更新點擊綠
	 */
	public void updateClickRate(Banner banner);
	
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Banner banner);
}
