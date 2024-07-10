package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.GSATNews;

/**
 * 外特專區Service介面接口
 */
public interface GSATNewsService {

	/**
	 * 取得外特專區資料清單(分頁)
	 */
	public List<GSATNews> getList(int pageCount, int pageStart, GSATNews gSATNews);
	
	/**
	 * 取得前台外特專區清單(依地區類別查詢)
	 */
	public List<GSATNews> getFrontList(GSATNews gSATNews);
	
	/**
	 * 取得前台外特專區資料清單(地區專用)
	 */
	public List<GSATNews> getFrontList(int pageCount, int pageStart, GSATNews gSATNews, List<Area> areaList, String search_sort);
	
	/**
	 * 取得前台外特專區資料清單(首頁專用，限制5筆)
	 */
	public List<GSATNews> getFrontList();
	
	/**
	 * 取得外特專區總筆數
	 */
	public Integer getCount(GSATNews gSATNews);
	
	/**
	 * 取得單筆外特專區
	 */
	public GSATNews getData(GSATNews gSATNews);
	
	/**
	 * 取得前台單筆外特專區
	 */
	public GSATNews getFrontData(GSATNews gSATNews);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增外特專區
	 */
	public void add(GSATNews gSATNews);
	
	/**
	 * 修改外特專區
	 */
	public void update(GSATNews gSATNews);
	
	/**
	 * 修改外特專區點擊率
	 */
	public void updateClickRate(GSATNews gSATNews);
	
	/**
	 * 刪除外特專區
	 */
	public void delete(GSATNews gSATNews ,Integer id);			
	
//	==========================
	/**
	 * 獲取正是機Menu
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Menu
	 */
	public void updateNormalData(GSATNews gSATNews);
	
	
	
}
