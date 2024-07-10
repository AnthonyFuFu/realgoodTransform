package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.SchoolBulletinContent;



public interface SchoolBulletinContentDao {
	/**
	 * 取得百官網公告內容資料清單
	 * @param schoolBulletinContent
	 * @return List<SchoolBulletinContent>
	 */
	public List<SchoolBulletinContent> getList(SchoolBulletinContent schoolBulletinContent);
	
	/**
	 * 取得百官網公告內容資料清單(分頁)
	 * @param schoolBulletinContent
	 * @param pageCount
	 * @param pageStart
	 * @return List<SchoolBulletinContent>
	 */
	public List<SchoolBulletinContent> getList(int pageCount, int pageStart, SchoolBulletinContent schoolBulletinContent);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增百官網公告內容
	 * @param schoolBulletinContent
	 */
	public void add(SchoolBulletinContent schoolBulletinContent);
	
	/**
	 * 修改百官網公告內容
	 * @param schoolBulletinContent
	 */
	public void update(SchoolBulletinContent schoolBulletinContent);
	
	/**
	 * 刪除百官網公告內容
	 * @param id
	 */
	public void delete(Integer id);
	
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(SchoolBulletinContent schoolBulletinContent);
	
}
