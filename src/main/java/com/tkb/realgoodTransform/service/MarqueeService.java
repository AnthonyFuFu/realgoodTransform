package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Marquee;



public interface MarqueeService {
	
	/**
	 * 取得首頁跑馬燈 資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param marquee
	 * @return List<Marquee>
	 */
	public List<Marquee> getList(int pageCount, int pageStart, Marquee marquee);	
	
	/**
	 * 取得首頁跑馬燈清單
	 * @param marquee
	 * @return List<Marquee>
	 */
	public List<Marquee> getFrontList(Marquee marquee);
	
	/**
	 * 取得跑馬燈 總筆數
	 * @param marquee
	 * @return Integer
	 */
	public Integer getCount(Marquee marquee);	
	
	/**
	 * 取得後台單筆首頁跑馬燈 
	 * @param marquee
	 * @return Marquee
	 */
	public Marquee getData(Marquee marquee);
	
	/**
	 * 取得前台單筆首頁跑馬燈 
	 * @param marquee
	 * @return Marquee
	 */
	public Marquee getFrontData(Marquee marquee);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增首頁跑馬燈 
	 * @param marquee
	 */
	public void add(Marquee marquee);
	
	/**
	 * 修改首頁跑馬燈 
	 * @param marquee
	 */
	public void update(Marquee marquee);
	
	/**
	 * 修改跑馬燈排序
	 * @param Marquee
	 */
	public void updateSort(Marquee marquee);
	/**
	 * 刪除首頁跑馬燈 
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 修改首頁跑馬燈 點擊率
	 * @param marquee
	 */
	public void updateClickRate(Marquee marquee);
	
	/**
	 * 重新排序跑馬燈
	 * @param marquee
	 */
	public void resetSort();
	
	
//	=======================
	/**
	 * 獲取正是機Bannerlist
	 */
	public List<Map<String, Object>> getNormalBannerList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Marquee marquee);
}
