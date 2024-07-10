package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
import com.tkb.realgoodTransform.model.SchoolBulletin;
import com.tkb.realgoodTransform.model.User;



public interface SchoolBulletinService {
	/**
	 * 依分類取得清單(後台使用)
	 * 
	 * @param schoolBulletin
	 * @return List<SchoolBulletin>
	 */
	public List<SchoolBulletin> getListByCategory(SchoolBulletin schoolBulletin);

	/**
	 * 取得百官網公告資料清單(分頁)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param schoolBulletin
	 * @return List<SchoolBulletin>
	 */
	public List<SchoolBulletin> getList(int pageCount, int pageStart, SchoolBulletin schoolBulletin, int groupId);

	/**
	 * 取得前台百官網公告清單(依地區類別查詢)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param schoolBulletin
	 * @return List<SchoolBulletin>
	 */
	public List<SchoolBulletin> getFrontList(SchoolBulletin schoolBulletin);

	/**
	 * 取得前台百官網公告資料清單(分頁)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param schoolBulletin
	 * @return List<SchoolBulletin>
	 */
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort);

	/**
	 * 取得前台百官網公告資料清單(分頁)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param schoolBulletin
	 * @return List<SchoolBulletin>
	 */
	public List<SchoolBulletin> getIndexFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			String search_sort);

	/**
	 * 取得前台百官網公告資料清單(地區專用)
	 * 
	 * @param pageCount
	 * @param pageStart
	 * @param schoolBulletin
	 * @param schoolBulletinList
	 * @return List<SchoolBulletin>
	 */
	public List<SchoolBulletin> getFrontList(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Area> areaList, String search_sort);

	/**
	 * 取得百官網公告總筆數
	 * 
	 * @param schoolBulletin
	 * @return Integer
	 */
	public Integer getCount(SchoolBulletin schoolBulletin);

	/**
	 * 取得前台百官網公告總筆數
	 * 
	 * @param schoolBulletin
	 * @return Integer
	 */
	public Integer getFrontCount(SchoolBulletin schoolBulletin);

	/**
	 * 取得單筆百官網公告
	 * 
	 * @param schoolBulletin
	 * @return SchoolBulletin
	 */
	public SchoolBulletin getData(SchoolBulletin schoolBulletin);

	/**
	 * 取得前台單筆百官網公告
	 * 
	 * @param schoolBulletin
	 * @return SchoolBulletin
	 */
	public SchoolBulletin getFrontData(SchoolBulletin schoolBulletin);

	/**
	 * 取得下一筆ID
	 * 
	 * @return Integer
	 */
	public Integer getNextId();

	/**
	 * 新增百官網公告
	 * 
	 * @param schoolBulletin
	 */
	public void add(SchoolBulletin schoolBulletin);

	/**
	 * 修改百官網公告
	 * 
	 * @param schoolBulletin
	 */
	public void update(SchoolBulletin schoolBulletin);

	/**
	 * 修改百官網公告點擊率
	 * 
	 * @param schoolBulletin
	 */
	public void updateClickRate(SchoolBulletin schoolBulletin);

	/**
	 * 刪除百官網公告
	 * 
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * 取全部公告
	 * 
	 * @param schoolBulletin
	 */
	public List<SchoolBulletin> getAllList(SchoolBulletin schoolBulletin);

	/**
	 * 修改百官網公告
	 * 
	 * @param schoolBulletin
	 */
	public void updateId(SchoolBulletin schoolBulletin);

	/**
	 * 權限查詢
	 * 
	 * @param user
	 */
	public Integer userAccount(User user);

	/**
	 * 取得隨機資料
	 * 
	 * @param schoolBulletin
	 */
	public List<SchoolBulletin> getRandomList(SchoolBulletin schoolBulletin);

	/**
	 * 取得上一篇文章
	 * 
	 * @param schoolBulletin
	 */
	public List<SchoolBulletin> getPrevList(SchoolBulletin schoolBulletin);

	/**
	 * 取得下一篇文章
	 * 
	 * @param schoolBulletin
	 */
	public List<SchoolBulletin> getNextList(SchoolBulletin schoolBulletin);

	public List<SchoolBulletin> getIndexList(SchoolBulletin schoolBulletin);

	/**
	 * ApiLocationCount
	 */
	public Integer getCountByApiLocation(SchoolBulletin schoolBulletin, List<Location> locationList);

	/**
	 * ApiLocationCount
	 */
	public List<SchoolBulletin> getListByApiLocation(int pageCount, int pageStart, SchoolBulletin schoolBulletin,
			List<Location> locationList, String search_sort);

	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	
	
	public List<Map<String, Object>> getNormalAreaList();
	
	/**
	 * 
	 * 
	 */
	public void updateNormalData(SchoolBulletin schoolBulletin);
	
	
	public void updateNormalAreaData(Area area);
}
