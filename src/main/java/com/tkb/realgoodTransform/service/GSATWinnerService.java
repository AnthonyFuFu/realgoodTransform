package com.tkb.realgoodTransform.service;

import java.util.List;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.GSATWinner;

/**
 * 外特專區Service介面接口
 */
public interface GSATWinnerService {

	/**
	 * 取得外特專區資料清單(分頁)
	 */
	public List<GSATWinner> getList(int pageCount, int pageStart, GSATWinner gSATWinner);
	
	/**
	 * 取得前台外特專區清單(依地區類別查詢)
	 */
	public List<GSATWinner> getFrontList(GSATWinner gSATWinner);
	
	/**
	 * 取得前台外特專區資料清單(分頁)
	 */
	public List<GSATWinner> getFrontList(int pageCount, int pageStart, GSATWinner gSATWinner);
	
	/**
	 * 取得前台外特專區資料清單(地區專用)
	 */
	public List<GSATWinner> getFrontList(int pageCount, int pageStart, GSATWinner gSATWinner, List<Area> areaList, String search_sort);
	
	/**
	 * 取得前台外特專區資料清單(首頁專用，限制5筆)
	 */
	public List<GSATWinner> getFrontList();
	
	/**
	 * 取得外特專區總筆數
	 */
	public Integer getCount(GSATWinner gSATWinner);
	
	/**
	 * 取得前台外特專區總筆數
	 */
	public Integer getFrontCount(GSATWinner gSATWinner);
	
	/**
	 * 取得單筆外特專區
	 */
	public GSATWinner getData(GSATWinner gSATWinner);
	
	/**
	 * 取得前台單筆外特專區
	 */
	public GSATWinner getFrontData(GSATWinner gSATWinner);
	
	/**
	 * 取得下一筆ID
	 */
	public Integer getNextId();
	
	/**
	 * 新增外特專區
	 */
	public void add(GSATWinner gSATWinner);
	
	/**
	 * 修改外特專區
	 */
	public void update(GSATWinner gSATWinner);
	
	/**
	 * 修改外特專區點擊率
	 */
	public void updateClickRate(GSATWinner gSATWinner);
	
	/**
	 * 刪除外特專區
	 */
	public void delete(GSATWinner gSATWinner ,Integer id);			
	
}
