package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Banner;



public interface BannerService {
	/**
	 * 取得首頁Banner 資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param banner
	 * @return List<Banner>
	 */
	public List<Banner> getList(int pageCount, int pageStart, Banner banner);
	
	/**
	 * 取得前台首頁Banner 清單(依地區類別查詢)
	 * @param pageCount
	 * @param pageStart
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
	 * 取得前台首頁Banner 總筆數
	 * @param banner
	 * @return Integer
	 */
	public Integer getFrontCount(Banner banner);

	/**
	 * 取得前台首頁Banner show總筆數
	 * @param banner
	 * @return Integer
	 */
	public Integer getShowCount(Banner banner);
	
	
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
	 * 修改首頁Banner 點擊率
	 * @param banner
	 */
	public void updateClickRate(Banner banner);
	
	/**
	 * 修改Banner排序
	 * @param Banner
	 */
	public void updateSort(Banner banner);
	/**
	 * 刪除首頁Banner 
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 重新排序Banner
	 */
	public void resetSort();
	

	
	
//	==========================
	/**
	 * 獲取正是機Bannerlist
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Banner banner);
}
